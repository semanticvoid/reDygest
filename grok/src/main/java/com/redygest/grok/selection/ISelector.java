package com.redygest.grok.selection;

import java.util.List;

import com.redygest.commons.data.Data;

public interface ISelector {
	List<Data> select(List<Data> data, List<String> members);
}
