package com.redygest.grok.evaluation;

import java.util.List;

import com.redygest.grok.evaluation.Data.Record;

public interface IEvaluator {
	public EvalMetrics evaluate(List<Record> goldset_records,
			List<Record> generated_records);
}
