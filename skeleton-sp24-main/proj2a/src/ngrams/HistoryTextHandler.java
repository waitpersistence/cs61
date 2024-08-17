package ngrams;
import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;

import java.util.List;
public class HistoryTextHandler extends NgordnetQueryHandler{
    public HistoryTextHandler(NGramMap map) {
        this.map = map;
    }
    public NGramMap map;
    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        String response = "";
        for(int i = 0; i <q.words().size();i++) {
            TimeSeries ts = map.weightHistory(q.words().get(i), q.startYear(), q.endYear());
//      for(Integer year:ts.keySet()){
            response = response + String.valueOf(q.words().get(i))+" {";
            response += ts.toString();
            response += " }"+"\n";
        }
        //}
        return response;
    }
}
