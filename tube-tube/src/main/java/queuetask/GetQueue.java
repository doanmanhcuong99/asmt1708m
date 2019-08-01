package queuetask;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import entity.Article;
import entity.CrawlerSource;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class GetQueue extends HttpServlet {
    Queue q = QueueFactory.getQueue("queue-green");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TaskHandle> tasks = q.leaseTasks(30, TimeUnit.SECONDS, 1);
        if (tasks.size() > 0) {
            TaskHandle task = tasks.get(0);
            String taskContent = new String(task.getPayload());
            JSONObject jsonObject = new JSONObject(taskContent);
            String link = jsonObject.getString("link");
            long sourceId = jsonObject.getLong("sourceId");
            CrawlerSource crawlerSource = ofy().load().type(CrawlerSource.class).id(sourceId).now();
            if (crawlerSource == null) {
                return;
            }
            System.out.println(link);
            Document document = Jsoup.connect(link).ignoreContentType(true).get();
            String title = document.select(crawlerSource.getTitleSelector()).text();
            String description = document.select(crawlerSource.getDescriptionSelector()).text();
            String content = document.select(crawlerSource.getContentSelector()).text();

            Article article = new Article(link);
            article.setTitle(title);
            article.setDescription(description);
            article.setContent(content);
            article.setCategoryId(crawlerSource.getCategoryId());
            article.setSourceId(crawlerSource.getId());
            ofy().save().entity(article).now();
            q.deleteTask(task);
        }
    }
}

