package queuetask;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.gson.Gson;
import entity.CrawlerSource;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class AddQueue extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddQueue.class.getSimpleName());
    private static Queue q = QueueFactory.getQueue("queue-green");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* Document document = Jsoup.connect("https://ione.vnexpress.net/tin-tuc/thoi-trang").ignoreContentType(true).get();
        org.jsoup.nodes.Element newsHome = document.getElementById("news_home");
        Elements elements = newsHome.select(".txt_link");
        for (Element el :
                elements) {
            String link = el.attr("href");
            System.out.println(link);
            q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(link));
        }*/

        List<CrawlerSource> crawlerSourceList = ofy().load().type(CrawlerSource.class).filter("status", 1).list();
        if (crawlerSourceList.size() == 0) {
            return;
        }
        for (CrawlerSource crawlerSource :
                crawlerSourceList) {
            Document document = Jsoup.connect(crawlerSource.getUrl()).ignoreContentType(true).get();
            Elements element = document.select(crawlerSource.getLinkSelector());
            for (Element el :
                    element) {
                String link = el.attr("href");
                HashMap<String, Object> hashMapQueue = new HashMap<>();
                hashMapQueue.put("link", link);
                hashMapQueue.put("sourceId", crawlerSource.getId());
                LOGGER.info(link);
                q.add(TaskOptions.Builder.withMethod(TaskOptions.Method.PULL).payload(new Gson().toJson(hashMapQueue)));

            }
            resp.getWriter().print("pull success");
        }
    }

}
