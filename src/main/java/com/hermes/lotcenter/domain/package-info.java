/**
 * 领域层，对数据源相关数据包装的相关逻辑放在此处，原则上所有的数据库查询，
 * redis调用，缓存都需要通过domian等对外暴露。此层是传统MVC结构的延伸，
 * 是从传统Service层延伸初的一层，传统Controller + Service + Dao层，
 * Service层太重，对于逻辑代码的复用性极不友好，同时也大大加大循环依赖的几率。
 */

package com.hermes.lotcenter.domain;


