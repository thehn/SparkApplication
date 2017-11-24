package kr.gaion.ceh.web.controller;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.gaion.ceh.web.config.AppSettingUtil;
import kr.gaion.ceh.web.model.EsWebConnector;

@Controller
public class DashBoard_DemoController {
	@RequestMapping(value= "/")
	public String showDashboard(ModelMap model) throws UnknownHostException {
		
		
		// to load settings to model
		AppSettingUtil.loadCurrentConfigToModel(model, BaseController.webConfig);
		
		// get data from Elasticsearch
		EsWebConnector connector = EsWebConnector.getInstance(BaseController.webConfig);
		TransportClient client = connector.getClient();
		
		SearchRequestBuilder srb = client.prepareSearch("etri").setTypes("ktme")
				.setQuery(QueryBuilders.matchQuery("defect_prob", "10")).setSize(0);
		MultiSearchResponse msr = client.prepareMultiSearch().add(srb).get();
		long msrTotalHit = msr.getResponses()[0].getResponse().getHits().getTotalHits();
		//System.out.println(msrTotalHit);

//		SearchRequestBuilder srbMulti = client.prepareSearch("etri").setTypes("ktme")
//				.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("defect_prob", "10")));
//		MultiSearchResponse msrMulti = client.prepareMultiSearch().add(srb).get();

		SearchRequestBuilder srbWGood = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "10"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrWGood = client.prepareMultiSearch().add(srbWGood).get();
		long wGoodCount = msrWGood.getResponses()[0].getResponse().getHits().getTotalHits();
		//System.out.println("Wheel Good >> " + msrWGood);
		
		SearchRequestBuilder srbWWarning = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "20"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrWWarning = client.prepareMultiSearch().add(srbWWarning).get();
		long wWarningCount = msrWWarning.getResponses()[0].getResponse().getHits().getTotalHits();
		//System.out.println("Wheel Warning >> " + msrWWarning);
		
		SearchRequestBuilder srbWFail = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "30"))
						.should(QueryBuilders.matchQuery("defect_prob", "40"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrWFail = client.prepareMultiSearch().add(srbWFail).get();
		long wFailCount = msrWFail.getResponses()[0].getResponse().getHits().getTotalHits();
		//System.out.println("Wheel Fail >> " + msrWFail);
		
		SearchRequestBuilder srbWNoOper = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", ""))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrWNoOper = client.prepareMultiSearch().add(srbWNoOper).get();
		long wNoOperCount = msrWNoOper.getResponses()[0].getResponse().getHits().getTotalHits();
		//System.out.println("Wheel NoOper >> " + msrWNoOper);

		SearchRequestBuilder srbBGood = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "10"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrBGood = client.prepareMultiSearch().add(srbBGood).get();
		long bGoodCount = msrBGood.getResponses()[0].getResponse().getHits().getTotalHits();
		
		SearchRequestBuilder srbBWarning = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "20"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrBWarning = client.prepareMultiSearch().add(srbBWarning).get();
		long bWarningCount = msrBWarning.getResponses()[0].getResponse().getHits().getTotalHits();
		
		SearchRequestBuilder srbBFail = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "30"))
						.should(QueryBuilders.matchQuery("defect_prob", "40"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrBFail = client.prepareMultiSearch().add(srbBFail).get();
		long bFailCount = msrBFail.getResponses()[0].getResponse().getHits().getTotalHits();
		
		SearchRequestBuilder srbBNoOper = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", ""))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse msrBNoOper = client.prepareMultiSearch().add(srbBNoOper).get();
		long bNoOperCount = msrBNoOper.getResponses()[0].getResponse().getHits().getTotalHits();
		
		//----------------------------------Good, Warning, Fail별 편성 ID 전체 조회
		SearchResponse srWGood = client.prepareSearch().setFetchSource(new String[] { "ID" }, null)
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "10"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")))
				.execute().actionGet();		
		HashMap<String, Object> mapGood = new HashMap<>();
		int iGood = 0;
		for (SearchHit hit : srWGood.getHits()) {
			mapGood.put(String.valueOf(iGood), hit.getSourceAsMap().get("ID"));
			iGood++;
		}
		
		SearchResponse srWWarning = client.prepareSearch().setFetchSource(new String[] { "ID" }, null)
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "20"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")))
				.execute().actionGet();		
		HashMap<String, Object> mapWarning = new HashMap<>();
		int iWarning = 0;
		for (SearchHit hit : srWWarning.getHits()) {
			mapWarning.put(String.valueOf(iWarning), hit.getSourceAsMap().get("ID"));
			iWarning++;
		}
		
		SearchResponse srWFail = client.prepareSearch().setFetchSource(new String[] { "ID" }, null)
				.setQuery(QueryBuilders.boolQuery()
						.must(QueryBuilders.matchQuery("defect_prob", "30"))
						.should(QueryBuilders.matchQuery("defect_prob", "40"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")))
				.execute().actionGet();		
		HashMap<String, Object> mapFail = new HashMap<>();
		int iFail = 0;
		for (SearchHit hit : srWFail.getHits()) {
			mapFail.put(String.valueOf(iFail), hit.getSourceAsMap().get("ID"));
			iFail++;
		}
		//----------------------------------여기까지

		//model.addAttribute("dataName", msr.toString());
		//model.addAttribute("dataCount", msrTotalHit);
		//model.addAttribute("test", srWGood.toString());
		model.addAttribute("wGoodCount", wGoodCount);
		model.addAttribute("wWarningCount", wWarningCount);
		model.addAttribute("wFailCount", wFailCount);
		model.addAttribute("wNoOperCount", wNoOperCount);
		model.addAttribute("bGoodCount", bGoodCount);
		model.addAttribute("bWarningCount", bWarningCount);
		model.addAttribute("bFailCount", bFailCount);
		model.addAttribute("bNoOperCount", bNoOperCount);
		
//		String w10_1 = "[{\"date\":\"2017/10/01\",\"value\":502.2},{\"date\":\"2017/10/02\",\"value\":523.6},{\"date\":\"2017/10/03\",\"value\":476},{\"date\":\"2017/10/04\",\"value\":486.5},{\"date\":\"2017/10/05\",\"value\":547.2},{\"date\":\"2017/10/06\",\"value\":551.2},{\"date\":\"2017/10/07\",\"value\":511.5},{\"date\":\"2017/10/08\",\"value\":513.2},{\"date\":\"2017/10/09\",\"value\":533.3},{\"date\":\"2017/10/10\",\"value\":525.6},{\"date\":\"2017/10/11\",\"value\":497.1},{\"date\":\"2017/10/12\",\"value\":494.4},{\"date\":\"2017/10/13\",\"value\":547.4},{\"date\":\"2017/10/14\",\"value\":503.4},{\"date\":\"2017/10/15\",\"value\":523.6}]";
//		String w10_2 = "[{\"date\":\"2017/10/01\",\"value\":188},{\"date\":\"2017/10/02\",\"value\":181},{\"date\":\"2017/10/03\",\"value\":180},{\"date\":\"2017/10/04\",\"value\":173},{\"date\":\"2017/10/05\",\"value\":163},{\"date\":\"2017/10/06\",\"value\":190},{\"date\":\"2017/10/07\",\"value\":155},{\"date\":\"2017/10/08\",\"value\":182},{\"date\":\"2017/10/09\",\"value\":179},{\"date\":\"2017/10/10\",\"value\":174},{\"date\":\"2017/10/11\",\"value\":211},{\"date\":\"2017/10/12\",\"value\":202},{\"date\":\"2017/10/13\",\"value\":188},{\"date\":\"2017/10/14\",\"value\":172},{\"date\":\"2017/10/15\",\"value\":191}]";
//		String w10_3 = "[{\"date\":\"2017/10/01\",\"value\":25},{\"date\":\"2017/10/02\",\"value\":23},{\"date\":\"2017/10/03\",\"value\":14},{\"date\":\"2017/10/04\",\"value\":27},{\"date\":\"2017/10/05\",\"value\":31},{\"date\":\"2017/10/06\",\"value\":20},{\"date\":\"2017/10/07\",\"value\":19},{\"date\":\"2017/10/08\",\"value\":24},{\"date\":\"2017/10/09\",\"value\":11},{\"date\":\"2017/10/10\",\"value\":14},{\"date\":\"2017/10/11\",\"value\":26},{\"date\":\"2017/10/12\",\"value\":16},{\"date\":\"2017/10/13\",\"value\":10},{\"date\":\"2017/10/14\",\"value\":27},{\"date\":\"2017/10/15\",\"value\":21}]";
//
//		String w20_1 = "[{\"date\":\"2017/10/01\",\"value\":25},{\"date\":\"2017/10/02\",\"value\":23},{\"date\":\"2017/10/03\",\"value\":14},{\"date\":\"2017/10/04\",\"value\":27},{\"date\":\"2017/10/05\",\"value\":31},{\"date\":\"2017/10/06\",\"value\":20},{\"date\":\"2017/10/07\",\"value\":19},{\"date\":\"2017/10/08\",\"value\":24},{\"date\":\"2017/10/09\",\"value\":11},{\"date\":\"2017/10/10\",\"value\":14},{\"date\":\"2017/10/11\",\"value\":26},{\"date\":\"2017/10/12\",\"value\":16},{\"date\":\"2017/10/13\",\"value\":10},{\"date\":\"2017/10/14\",\"value\":27},{\"date\":\"2017/10/15\",\"value\":21}]";
//		String w20_2 = "[{\"date\":\"2017/10/01\",\"value\":502.2},{\"date\":\"2017/10/02\",\"value\":523.6},{\"date\":\"2017/10/03\",\"value\":476},{\"date\":\"2017/10/04\",\"value\":486.5},{\"date\":\"2017/10/05\",\"value\":547.2},{\"date\":\"2017/10/06\",\"value\":551.2},{\"date\":\"2017/10/07\",\"value\":511.5},{\"date\":\"2017/10/08\",\"value\":513.2},{\"date\":\"2017/10/09\",\"value\":533.3},{\"date\":\"2017/10/10\",\"value\":525.6},{\"date\":\"2017/10/11\",\"value\":497.1},{\"date\":\"2017/10/12\",\"value\":494.4},{\"date\":\"2017/10/13\",\"value\":547.4},{\"date\":\"2017/10/14\",\"value\":503.4},{\"date\":\"2017/10/15\",\"value\":523.6}]";
//		String w20_3 = "[{\"date\":\"2017/10/01\",\"value\":188},{\"date\":\"2017/10/02\",\"value\":181},{\"date\":\"2017/10/03\",\"value\":180},{\"date\":\"2017/10/04\",\"value\":173},{\"date\":\"2017/10/05\",\"value\":163},{\"date\":\"2017/10/06\",\"value\":190},{\"date\":\"2017/10/07\",\"value\":155},{\"date\":\"2017/10/08\",\"value\":182},{\"date\":\"2017/10/09\",\"value\":179},{\"date\":\"2017/10/10\",\"value\":174},{\"date\":\"2017/10/11\",\"value\":211},{\"date\":\"2017/10/12\",\"value\":202},{\"date\":\"2017/10/13\",\"value\":188},{\"date\":\"2017/10/14\",\"value\":172},{\"date\":\"2017/10/15\",\"value\":191}]";
//
//		String w30_1 = "[{\"date\":\"2017/10/01\",\"value\":188},{\"date\":\"2017/10/02\",\"value\":181},{\"date\":\"2017/10/03\",\"value\":180},{\"date\":\"2017/10/04\",\"value\":173},{\"date\":\"2017/10/05\",\"value\":163},{\"date\":\"2017/10/06\",\"value\":190},{\"date\":\"2017/10/07\",\"value\":155},{\"date\":\"2017/10/08\",\"value\":182},{\"date\":\"2017/10/09\",\"value\":179},{\"date\":\"2017/10/10\",\"value\":174},{\"date\":\"2017/10/11\",\"value\":211},{\"date\":\"2017/10/12\",\"value\":202},{\"date\":\"2017/10/13\",\"value\":188},{\"date\":\"2017/10/14\",\"value\":172},{\"date\":\"2017/10/15\",\"value\":191}]";
//		String w30_2 = "[{\"date\":\"2017/10/01\",\"value\":25},{\"date\":\"2017/10/02\",\"value\":23},{\"date\":\"2017/10/03\",\"value\":14},{\"date\":\"2017/10/04\",\"value\":27},{\"date\":\"2017/10/05\",\"value\":31},{\"date\":\"2017/10/06\",\"value\":20},{\"date\":\"2017/10/07\",\"value\":19},{\"date\":\"2017/10/08\",\"value\":24},{\"date\":\"2017/10/09\",\"value\":11},{\"date\":\"2017/10/10\",\"value\":14},{\"date\":\"2017/10/11\",\"value\":26},{\"date\":\"2017/10/12\",\"value\":16},{\"date\":\"2017/10/13\",\"value\":10},{\"date\":\"2017/10/14\",\"value\":27},{\"date\":\"2017/10/15\",\"value\":21}]";
//		String w30_3 = "[{\"date\":\"2017/10/01\",\"value\":502.2},{\"date\":\"2017/10/02\",\"value\":523.6},{\"date\":\"2017/10/03\",\"value\":476},{\"date\":\"2017/10/04\",\"value\":486.5},{\"date\":\"2017/10/05\",\"value\":547.2},{\"date\":\"2017/10/06\",\"value\":551.2},{\"date\":\"2017/10/07\",\"value\":511.5},{\"date\":\"2017/10/08\",\"value\":513.2},{\"date\":\"2017/10/09\",\"value\":533.3},{\"date\":\"2017/10/10\",\"value\":525.6},{\"date\":\"2017/10/11\",\"value\":497.1},{\"date\":\"2017/10/12\",\"value\":494.4},{\"date\":\"2017/10/13\",\"value\":547.4},{\"date\":\"2017/10/14\",\"value\":503.4},{\"date\":\"2017/10/15\",\"value\":523.6}]";
		
		return "dashboard_demo";
	}
}
