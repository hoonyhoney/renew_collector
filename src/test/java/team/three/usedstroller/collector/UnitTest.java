package team.three.usedstroller.collector;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;
import team.three.usedstroller.collector.domain.dto.carrot.CarrotDto;
import team.three.usedstroller.collector.util.CarrotParser;
import team.three.usedstroller.collector.util.UnitConversionUtils;

class UnitTest {

  UnitConversionUtils unitConversionUtils;

  @Test
  void convert_date() {
    long timeStamp = 1706951909L;
    LocalDate date = Instant.ofEpochSecond(timeStamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
    System.out.println("date = " + date);
  }

  @Test
  void year_month_parse() {
    //given
    //when
    LocalDate parse1 = LocalDate.parse("2023-08-01");
    LocalDate parse2 = LocalDate.parse("2023.08.01", DateTimeFormatter.ofPattern("yyyy.MM.dd"));

    //then
    assertThat(parse1).isEqualTo(LocalDate.of(2023, 8, 1));
    assertThat(parse2).isEqualTo(LocalDate.of(2023, 8, 1));
  }

  @Test
  void change_int() {
    //given
    String releaseYear = "2023년도";

    //when
    String regex = "[^0-9]";
    String result = releaseYear.replaceAll(regex, "");
    int year = Integer.parseInt(result);

    //then
    assertThat(year).isEqualTo(2023);
  }

  @Test
  void change_price() {
    //given
    String before1 = "1,245,567원";
    String before2 = "나눔\uD83E\uDDE1"; //나눔🧡
    String before3 = "가격없음";

    //when
    String regex1 = "[,만원]";
    String regex2 = "[나눔\uD83E\uDDE1|가격없음]";

    String result1 = before1.replaceAll(regex1, "");
    long price = Long.parseLong(result1);
    String result2 = before2.replaceAll(regex2, "");
    System.out.println("result2 = " + result2);
    String result3 = before3.replaceAll(regex2, "");

    //then
    assertThat(price).isEqualTo(1245567);
    assertThat(result2).isEmpty();
    assertThat(result3).isEmpty();
  }

  @Test
  void regex() {
    //given
    String url1 = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/116.0.5845.96/linux64/chromedriver-linux64.zip";
    String url2 = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/116.0.5845.96/mac-arm64/chromedriver-mac-arm64.zip";
    String url3 = "https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/116.0.5845.96/win64/chromedriver-win64.zip";

    //when
    String regex = "linux64|arm64|win64";
    Pattern pattern = Pattern.compile(regex);
    Matcher linux = pattern.matcher(url1);
    Matcher macArm = pattern.matcher(url2);
    Matcher win = pattern.matcher(url3);

    //then
    if (linux.find()) {
      assertThat(linux.find()).isTrue();
      assertThat(linux.group()).isEqualTo("linux64");
    } else if (macArm.find()) {
      assertThat(macArm.find()).isTrue();
      assertThat(macArm.group()).isEqualTo("arm64");
    } else if (win.find()) {
      assertThat(win.find()).isTrue();
      assertThat(win.group()).isEqualTo("win64");
    }
  }

  @Test
  void convertLocalDate() {
    Long updateTime = 1712358260488L;
    //Long updateTime = 1712358260L;
    LocalDate localDate = Instant.ofEpochSecond(updateTime)
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
    System.out.println("localDate = " + localDate);
  }

  @Test
  void convertLocalDate2() {
    Long updateTime = 1712358260488L;
    //Long updateTime = 1712358260L;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
    LocalDate str = LocalDate.parse(sdf.format(new Date(updateTime)));
    System.out.println("str = " + str);
  }

  @Test
  @DisplayName("throw 에러")
  void exceptionTest() {
    for (int i = 0; i < 10; i++) {
      method(i);
    }
  }

  @Test
  @DisplayName("throws 에러")
  void exceptionTest1() {
    for (int i = 0; i < 10; i++) {
      try {
        method(i);
      } catch (RuntimeException e) {
        throw new IllegalArgumentException("메인 Exception");
      }
    }
  }

  @Test
  @DisplayName("걍 진행")
  void exceptionTest2() {
    for (int i = 0; i < 10; i++) {
      try {
        method(i);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
        System.out.println("에러떳지만 걍 진행 고고");
      }
    }
  }

  void method(int i) {
    if (i == 8) {
      throw new IllegalArgumentException("메서드 exception");
    }
    System.out.println("i = " + i);
  }

  @Test
  void conversionDate() {
    String testStr = "\n"
        + "            끌올 2달 전\n"
        + "          ";

    String testStr2 = "\n"
        + "             6분 전\n"
        + "          ";

    String uploadTime =
        ObjectUtils.isEmpty(testStr) ? "" : testStr.replace("끌올", "");
    System.out.println("uploadTime = " + uploadTime);
    String s = unitConversionUtils.convertToTimeFormat(uploadTime);
    System.out.println("s = " + s);
    LocalDate localDate = unitConversionUtils.changeLocalDate(s);
    System.out.println("localDate = " + localDate);
  }

  @Test
  void scriptParser() {
    String url = "https://www.daangn.com/kr/buy-sell/?in=역삼동-6035&search=부가부";
    CarrotParser carrotParser = new CarrotParser();
    List<CarrotDto> s = carrotParser.parseScript(url);
    for (CarrotDto carrotDto : s) {
      System.out.println("carrotDto = " + carrotDto);
    }
  }


  @Test
  void parseString() {
    String id = "\"/kr/buy-sell/%EB%B2%A0%EC%9D%B4%EB%B9%84%EC%A0%A0-%EC%9A%94%EC%9A%94-6-%EC%9C%A0%EB%AA%A8%EC%B0%A8-%EB%B0%B1-%EB%AA%A8%EA%B8%B0%EC%9E%A5-%EC%83%88%EC%83%81%ED%92%88-t2fm3t3ptoyi/";
    String[] segments = id.split("/");
    String segment = segments[segments.length - 1];
    String[] pid = segment.split("-");
    String newId = pid[pid.length - 1];
    System.out.println("newId = " + newId);

    String price = "100000.0";
    String[] priceArray = price.split("\\.");
    System.out.println("price = " + Long.parseLong(priceArray[0]));
  }

  @Test
  @DisplayName("당근 시간 파싱하기")
  void carrotTimeParsing() {
    String url = "https://www.daangn.com/kr/buy-sell/%EB%B6%80%EA%B0%80%EB%B6%80%ED%8F%AD%EC%8A%A45-%EC%9C%A0%EB%AA%A8%EC%B0%A8-%ED%8C%94%EC%95%84%EC%9A%94-kdbvnpa2gg76/?in=%EA%B0%95%EC%84%9C%EA%B5%AC-257";
    try {
      Document doc = Jsoup.connect(url).get();
      String time = doc.selectFirst("time").toString();
      String[] split = time.split("=");
      String s = split[1];
      String substring = s.substring(1, 11);
      System.out.println("substring = " + substring);

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}