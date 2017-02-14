package io.ibole.infrastructure.support.tool.codegen.pojo;

import java.util.ArrayList;
import java.util.List;

public class TabPojo {

	private String tabName;
	private List<ColPojo> cols = new ArrayList<ColPojo>();
	private List<String> pks = new ArrayList<String>();
	
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}
	public List<ColPojo> getCols() {
		return cols;
	}
	public void setCols(List<ColPojo> cols) {
		this.cols = cols;
	}
	public List<String> getPks() {
		return pks;
	}
	public void setPks(List<String> pks) {
		this.pks = pks;
	}
	
}
