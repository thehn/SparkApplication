package kr.gaion.ceh.web.controller;

import java.net.UnknownHostException;
import java.util.Map;

import org.apache.lucene.search.TermQuery;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.gaion.ceh.web.model.EsWebConnector;

@Controller
public class EtriDashBoardController {

	@RequestMapping(value = "/dashboard_test")
	public String showDashboard(ModelMap model) throws UnknownHostException {

		// get data from Elasticsearch
		EsWebConnector connector = EsWebConnector.getInstance(BaseController.webConfig);

		// transform the data
		TransportClient client = connector.getClient();
		SearchRequestBuilder srb = client.prepareSearch("etri").setTypes("ktme")
				.setQuery(QueryBuilders.matchQuery("defect_prob", "10")).setSize(0);
		MultiSearchResponse msr = client.prepareMultiSearch().add(srb).get();
		long msr2 = msr.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(msr2);

		// what does this block do?
		SearchRequestBuilder srbMulti = client.prepareSearch("etri").setTypes("ktme")
				.setQuery(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("defect_prob", "10")));
		MultiSearchResponse msrMulti = client.prepareMultiSearch().add(srb).get();

		// historic data
		// SearchRequestBuilder srbAggs =
		// client.prepareSearch("etri").setTypes("ktme").setSize(0)
		// .setQuery(QueryBuilders.matchQuery("defect_prob",
		// "10")).addAggregation(aggregation)
		SearchRequestBuilder a = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "10"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		System.out.println("a =  " + a);
		MultiSearchResponse a1 = client.prepareMultiSearch().add(a).get();
		
		SearchResponse a2 = client.prepareSearch().setFetchSource(new String[] { "ID" }, null)
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "10"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")))
				.execute().actionGet();

		for (SearchHit hit : a2.getHits()) {
			Map<String, Object> map = hit.getSourceAsMap();
			map.toString();
		}

		long w10 = a1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(a1);
		SearchRequestBuilder b = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "20"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse b1 = client.prepareMultiSearch().add(b).get();
		long w20 = b1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(b1);
		SearchRequestBuilder c = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "30"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse c1 = client.prepareMultiSearch().add(c).get();
		long w30 = c1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(c1);
		SearchRequestBuilder d = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "40"))
						.must(QueryBuilders.matchQuery("type_WB", "W"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse d1 = client.prepareMultiSearch().add(d).get();
		long w40 = d1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(d1);

		SearchRequestBuilder e = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "10"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse e1 = client.prepareMultiSearch().add(e).get();
		long b10 = e1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(e1);
		SearchRequestBuilder f = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "20"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse f1 = client.prepareMultiSearch().add(f).get();
		long b20 = f1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(f1);
		SearchRequestBuilder g = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "30"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse g1 = client.prepareMultiSearch().add(g).get();
		long b30 = g1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(g1);
		SearchRequestBuilder h = client.prepareSearch("etri")
				.setQuery(QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("defect_prob", "40"))
						.must(QueryBuilders.matchQuery("type_WB", "B"))
						.must(QueryBuilders.matchQuery("_time", "2017-11-08")));
		MultiSearchResponse h1 = client.prepareMultiSearch().add(h).get();
		long b40 = h1.getResponses()[0].getResponse().getHits().getTotalHits();
		System.out.println(h1);

		// msr.getResponses()[0].getResponse().getHits()

		// use ModelMap to pass data to JSP
		String data = "this is data example  !!!";
		model.addAttribute("dataName", msr.toString());
		model.addAttribute("dataCount", msr2);
		model.addAttribute("w10", w10);
		model.addAttribute("w20", w20);
		model.addAttribute("w30", w30);
		model.addAttribute("w40", w40);
		model.addAttribute("b10", b10);
		model.addAttribute("b20", b20);
		model.addAttribute("b30", b30);
		model.addAttribute("b40", b40);

		model.addAttribute("test", a2.toString());

		return "dashboard_test";
	}
}
