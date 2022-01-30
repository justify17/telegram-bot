package Commands;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransportStop {
    private  List<String> allTrolleybusesAtStop = new ArrayList<>();
    private List<String> allBusesAtStop = new ArrayList<>();
    private String stopName;
    private  List<String> finalTrolleybuses = new ArrayList<>();
    private List<String> finalBuses = new ArrayList<>();

    public List<String> getFinalTrolleybuses() {
        return finalTrolleybuses;
    }

    public List<String> getFinalBuses() {
        return finalBuses;
    }

    public TransportStop(String stopName){
        this.stopName = stopName;
    }

    public String URLFormatting(){
        return "https://kogda.by/stops/gomel/"+stopName;
    }

    public String pageParsing(){
        Document doc = null;
        try {
            doc = Jsoup.connect(URLFormatting()).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements newsHeadlines = doc.select("div.transport-block");
        StringBuilder sb = new StringBuilder();
        for (Element headline : newsHeadlines) {
            sb.append(headline.text()+" ");
        }
        return sb.toString();
    }

    public void searchAllTrolleybusesAtStop(String str){
        Pattern pattern = Pattern.compile("Троллейбусы[^А-Я]+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            pattern = Pattern.compile("[\\d]+[а-яА-Я]?");
            matcher = pattern.matcher(matcher.group());
            while(matcher.find()){
                allTrolleybusesAtStop.add(matcher.group());
            }
        }
        System.out.println(allTrolleybusesAtStop);
    }

    public void searchAllBusesAtStop(String str){
        Pattern pattern = Pattern.compile("Автобусы[^А-Я]+");
        Matcher matcher = pattern.matcher(str);
        while(matcher.find()){
            pattern = Pattern.compile("[\\d]+[а-яА-Я]?");
            matcher = pattern.matcher(matcher.group());
            while(matcher.find()){
                allBusesAtStop.add(matcher.group());
            }
        }
        System.out.println(allBusesAtStop);
    }

    public void searcher(){
        String stop = pageParsing();
        searchAllTrolleybusesAtStop(stop);
        searchAllBusesAtStop(stop);
    }

    public String getStopName() {
        return stopName;
    }

    public List<String> getAllTrolleybusesAtStop() {
        return allTrolleybusesAtStop;
    }

    public List<String> getAllBusesAtStop() {
        return allBusesAtStop;
    }

    public void searchDesiredRoutesTrolleybuses(TransportStop tr){
        for (String strOne : this.getAllTrolleybusesAtStop()) {
            for (String string : tr.allTrolleybusesAtStop) {
                if (strOne.equals(string)) {
                    finalTrolleybuses.add(strOne);
                }
            }
        }
        System.out.println(finalTrolleybuses);
    }

    public void searchDesiredRoutesBuses(TransportStop tr){
        for (String strOne : this.getAllBusesAtStop()) {
            for (String string : tr.getAllBusesAtStop()) {
                if (strOne.equals(string)) {
                    finalBuses.add(strOne);
                }
            }
        }
        System.out.println(finalBuses);
    }
}