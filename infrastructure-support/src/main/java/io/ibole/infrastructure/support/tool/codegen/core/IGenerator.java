package io.ibole.infrastructure.support.tool.codegen.core;

import io.ibole.infrastructure.support.tool.codegen.model.TableModel;

import java.io.IOException;

import freemarker.template.TemplateException;

/**
*@author:    whm
*@createtime:2016年5月19日
*/
public interface IGenerator {

	public void build(TableModel tm,boolean cover) throws TemplateException,IOException;
}

