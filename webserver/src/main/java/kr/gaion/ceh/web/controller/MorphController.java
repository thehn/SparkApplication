package kr.gaion.ceh.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.bydelta.koala.data.Sentence;
import kr.bydelta.koala.kkma.Parser;
import kr.bydelta.koala.kkma.Tagger;
import kr.bydelta.koala.twt.SentenceSplitter;
import kr.gaion.ceh.web.controller.MorphController;
import kr.gaion.ceh.web.model.Morph;

@Controller
public class MorphController {

	private static final Logger logger = LoggerFactory.getLogger(MorphController.class);

	@RequestMapping(value = "/morph", method = RequestMethod.GET)
	public String morpheme(Locale locale, Model model) throws IOException {
		logger.info("Morph page");
		return "morph";
	}

	@RequestMapping(value = "/morph/analysis", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
	public @ResponseBody String analysis(@ModelAttribute("vo") Morph vo, Model model) throws IOException {
		logger.info("Morpheme Page");
		String contents = vo.getContents();
		SentenceSplitter splitter = new SentenceSplitter();
		Tagger tagger = new Tagger(null);
		Parser parser = new Parser();
		List<String> sentences = splitter.jSentences(contents);

		// List<Map<String, Object>> list = new ArrayList<Map<String,
		// Object>>();;
		StringBuilder dataBuilder = new StringBuilder();
		dataBuilder.append("[");
		int no = 0;
		for (String line : sentences) {
			Sentence sentence = tagger.tagSentence(line);
			Sentence parsed = parser.parse(sentence);
			String[] terms = parsed.singleLineString().split(" ");
			for (String temp : terms) {
				String[] term = temp.split("\\+");
				for (String word : term) {
					String[] words = word.split("\\/");
					String tag = partOfSpeech(words[1].trim());
					no++;
					dataBuilder.append("[\"" + no + "\", \"" + words[0].trim() + "\", \"" + words[1].trim() + "\", \""
							+ tag + "\"],");
				}

			}
		}
		dataBuilder.deleteCharAt(dataBuilder.length() - 1);
		dataBuilder.append("]");

		String json = dataBuilder.toString();
		System.out.println(json);

//		model.addAttribute("data", json);

		return json;
	}

	@RequestMapping(value = "/morph/stt", method = RequestMethod.GET)
	public String stt(Locale locale, Model model) throws IOException {
		logger.info("STT page");
		return "stt";
	}

	public String partOfSpeech(String tag) {
		String lavel = "";
		switch (tag) {
		case "NNG":
			lavel = "일반 명사";
			break;
		case "NNP":
			lavel = "고유 명사";
			break;
		case "NNB":
			lavel = "일반 의존 명사";
			break;
		case "NNM":
			lavel = "단위 의존 명사";
			break;
		case "NR":
			lavel = "수사";
			break;
		case "NP":
			lavel = "대명사";
			break;
		case "VV":
			lavel = "동사";
			break;
		case "VA":
			lavel = "형용사";
			break;
		case "VX":
			lavel = "보조 용언";
			break;
		case "VXV":
			lavel = "보조 동사";
			break;
		case "VXA":
			lavel = "보조 형용사";
			break;
		case "VCP":
			lavel = "긍정 지정사";
			break;
		case "VCN":
			lavel = "부정 지정사";
			break;
		case "MM":
			lavel = "관형사";
			break;
		case "MDT":
			lavel = "일반 관형사";
			break;
		case "MDN":
			lavel = "수 관형사";
			break;
		case "MAG":
			lavel = "일반 부사";
			break;
		case "MAJ":
			lavel = "접속 부사";
			break;
		case "MAC":
			lavel = "접속 부사";
			break;
		case "IC":
			lavel = "감탄사";
			break;
		case "JKS":
			lavel = "주격 조사";
			break;
		case "JKC":
			lavel = "보격 조사";
			break;
		case "JKG":
			lavel = "관형격 조사";
			break;
		case "JKO":
			lavel = "목적격 조사";
			break;
		case "JKB":
			lavel = "부사격 조사";
			break;
		case "JKM":
			lavel = "부사격 조사";
			break;
		case "JKV":
			lavel = "호격 조사";
			break;
		case "JKI":
			lavel = "호격 조사";
			break;
		case "JKQ":
			lavel = "인용격 조사";
			break;
		case "JX":
			lavel = "보조사";
			break;
		case "JC":
			lavel = "접속 조사";
			break;
		case "EP":
			lavel = "선어말 어미";
			break;
		case "EPH":
			lavel = "존칭 선어말 어미";
			break;
		case "EPT":
			lavel = "시제 선어말 어미";
			break;
		case "EPP":
			lavel = "공손 선어말 어미";
			break;
		case "EF":
			lavel = "종결 어미";
			break;
		case "EFN":
			lavel = "평서형 종결 어미";
			break;
		case "EFQ":
			lavel = "의문형 종결 어미";
			break;
		case "EFO":
			lavel = "명령형 종결 어미";
			break;
		case "EFA":
			lavel = "청유형 종결 어미";
			break;
		case "EFI":
			lavel = "감탄형 종결 어미";
			break;
		case "EFR":
			lavel = "존칭형 종결 어미";
			break;
		case "EC":
			lavel = "연결 어미";
			break;
		case "ECE":
			lavel = "대등 연결 어미";
			break;
		case "ECD":
			lavel = "의존적 연결 어미";
			break;
		case "ECS":
			lavel = "보조적 연결 어미";
			break;
		case "ETN":
			lavel = "명사형 전성 어미";
			break;
		case "ETM":
			lavel = "관형형 전성 어미";
			break;
		case "ETD":
			lavel = "관형형 전성 어미";
			break;
		case "XPN":
			lavel = "체언 접두사";
			break;
		case "XPV":
			lavel = "용언 접두사";
			break;
		case "XSN":
			lavel = "명사 파생 접미사";
			break;
		case "XSV":
			lavel = "동사 파생 접미사";
			break;
		case "XSA":
			lavel = "형용사 파생 접미사";
			break;
		case "XR":
			lavel = "어근";
			break;
		case "SF":
			lavel = "마침표물음표,느낌표";
			break;
		case "SP":
			lavel = "쉼표,가운뎃점,콜론,빗금";
			break;
		case "SS":
			lavel = "따옴표,괄호표,줄표";
			break;
		case "SE":
			lavel = "줄임표";
			break;
		case "SO":
			lavel = "붙임표(물결,숨김,빠짐)";
			break;
		case "SW":
			lavel = "기타기호 (논리수학기호,화폐기호)";
			break;
		case "NF":
			lavel = "명사추정범주";
			break;
		case "UN":
			lavel = "명사추정범주";
			break;
		case "NV":
			lavel = "용언추정범주";
			break;
		case "NA":
			lavel = "분석불능범주";
			break;
		case "SL":
			lavel = "외국어";
			break;
		case "OL":
			lavel = "외국어";
			break;
		case "SH":
			lavel = "한자";
			break;
		case "OH":
			lavel = "한자";
			break;
		case "SN":
			lavel = "숫자";
			break;
		case "ON":
			lavel = "숫자";
			break;
		default:
			break;
		}

		return lavel;
	}
}
