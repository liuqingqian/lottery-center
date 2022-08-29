package com.hermes.lotcenter.domain;

import cn.hutool.core.util.NumberUtil;
import com.hermes.lotcenter.domain.dto.LotRecordDTO;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.tags.TableTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by liuqingqian on 2022/8/29.
 */
@Component
public class LotTableParserDomain {

    private static final String ATTRIBUTE_CLASS = "class";
    private static final String TABLE_CLASS_NAME = "awardList";


    public List<LotRecordDTO> parseLotTable(String code, String content) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        if (StringUtils.isBlank(content)) {
            return null;
        }
        //解析html表单字段；
        Parser mParser = null;
        NodeList nodeList = null;
        mParser = Parser.createParser(content, "utf-8");
        NodeFilter tableFilter = new NodeClassFilter(TableTag.class);
        OrFilter lastFilter = new OrFilter();
        lastFilter.setPredicates(new NodeFilter[]{tableFilter});
        List<LotRecordDTO> lotRecordList = new ArrayList<>();
        try {
            nodeList = mParser.parse(lastFilter);
            //循环读取每一个Table
            for (int i = 0; i <= nodeList.size(); i++) {
                if (nodeList.elementAt(i) instanceof TableTag) {
                    TableTag tableTag = (TableTag) nodeList.elementAt(i);
                    //只取开奖记录表格的数据
                    if (isNotAwardListTableTag(tableTag)) {
                        break;
                    }
                    TableRow[] rows = tableTag.getRows();
                    for (int j = 0; j < rows.length; j++) {
                        TableRow tr = rows[j];
                        //解析表单的一行
                        LotRecordDTO lotRecordDTO = parseTableRow(code, tr);
                        if (Objects.nonNull(lotRecordDTO)) {
                            lotRecordList.add(lotRecordDTO);
                        }
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }

        return lotRecordList.stream()
                .filter(recordDTO -> StringUtils.isNotBlank(recordDTO.getPeriodNumber()))
                .sorted(Comparator.comparing(LotRecordDTO::getPeriodNumber))
                .collect(Collectors.toList());
    }


    /**
     * 解析开奖记录表单的一行
     *
     * @param tr
     * @return
     */
    private LotRecordDTO parseTableRow(String code, TableRow tr) {
        if (Objects.isNull(tr) || ArrayUtils.isEmpty(tr.getColumns())) {
            return null;
        }
        TableColumn[] tds = tr.getColumns();
        //读取每一个单元格
        //期数 作为主键
        String periodNumber = parseTrimTxt(tds[RowColumn.PERIOD_NUMBER].getStringText());
        if (StringUtils.isBlank(periodNumber)) {
            return null;
        }
        LotRecordDTO lotRecord = new LotRecordDTO();
        lotRecord.setCode(code);
        lotRecord.setPeriodNumber(periodNumber);
        //开奖时间
        lotRecord.setLotTime(parseTrimTxt(tds[RowColumn.LOT_TIME].getStringText()));
        lotRecord.setDice1(parseDice(tds[RowColumn.DICE1].getStringText()));
        lotRecord.setDice2(parseDice(tds[RowColumn.DICE2].getStringText()));
        lotRecord.setDice3(parseDice(tds[RowColumn.DICE3].getStringText()));
        lotRecord.setSum(parseSum(tds[RowColumn.SUM].getStringText()));
        lotRecord.setSizeTxt(parseSize(tds[RowColumn.SIZE].getStringText()));
        lotRecord.setSingleDoubleTxt(parseSingleDouble(tds[RowColumn.SINGLE_DOUBLE].getStringText()));

        //豹
        boolean isEquals = Objects.equals(lotRecord.getDice1(), lotRecord.getDice2())
                && Objects.equals(lotRecord.getDice2(), lotRecord.getDice3());
        lotRecord.setIsEquals(isEquals);
        //状态
        if (Objects.equals(lotRecord.getSum(), lotRecord.getDice1() + lotRecord.getDice2() + lotRecord.getDice3())) {
            lotRecord.setLotStatus(1);
        }
        return lotRecord;

    }

    private String parseTrimTxt(String tdTxt) {
        if (StringUtils.isBlank(tdTxt)) {
            return null;
        }
        return tdTxt.trim();
    }

    private Integer parseDice(String tdTxt) {
        if (StringUtils.isBlank(tdTxt)) {
            return null;
        }
        //<i style="margin-left: 40%;" class="ball-2"></i>
        String diceTxt = tdTxt.trim()
                .replace("<i style=\"margin-left: 40%;\" class=\"ball-", "")
                .replace("\"></i>", "");
        if (NumberUtil.isNumber(diceTxt)) {
            return Integer.valueOf(diceTxt);
        }
        return null;
    }

    private Integer parseSum(String tdTxt) {
        if (StringUtils.isBlank(tdTxt)) {
            return null;
        }
        //<em class="smallBlueball">14</em>
        String sumTxt = tdTxt.trim()
                .replace("<em class=\"smallBlueball\">", "")
                .replace("</em>", "");
        if (NumberUtil.isNumber(sumTxt)) {
            return Integer.valueOf(sumTxt);
        }
        return null;
    }

    private String parseSize(String tdTxt) {
        if (StringUtils.isBlank(tdTxt)) {
            return null;
        }
        //<em class="big">大</em>
        if (tdTxt.contains("大")) {
            return "大";
        } else if (tdTxt.contains("小")) {
            return "小";
        }
        return null;
    }

    private String parseSingleDouble(String tdTxt) {
        if (StringUtils.isBlank(tdTxt)) {
            return null;
        }
        //<em class="even">双</em>
        if (tdTxt.contains("双")) {
            return "双";
        } else if (tdTxt.contains("单")) {
            return "单";
        }
        return null;
    }

    /**
     * 判断是否为开奖记录的表单标签
     *
     * @param tag
     * @return
     */
    private boolean isAwardListTableTag(TableTag tag) {
        if (Objects.isNull(tag)) {
            return false;
        }
        String aClass = tag.getAttribute(ATTRIBUTE_CLASS);
        return TABLE_CLASS_NAME.equalsIgnoreCase(aClass.trim());
    }

    private boolean isNotAwardListTableTag(TableTag tag) {
        return !isAwardListTableTag(tag);
    }

    private static class RowColumn {
        //期数
        private static final int PERIOD_NUMBER = 0;
        //开奖时间
        private static final int LOT_TIME = 1;
        //骰1
        private static final int DICE1 = 2;
        //骰2
        private static final int DICE2 = 3;
        //骰3
        private static final int DICE3 = 4;
        //和-彩球号码
        private static final int SUM = 5;
        //大小
        private static final int SIZE = 6;
        //单双
        private static final int SINGLE_DOUBLE = 7;

//        20220824001
//        2022-08-24 00:00:00
//<i style="margin-left: 40%;" class="ball-2"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//<em class="smallBlueball">14</em>
//<em class="big">大</em>
//<em class="even">双</em>
//<i style="margin-left: 40%;" class="ball-2"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
//<i style="margin-left: 40%;" class="ball-6"></i>
    }

}
