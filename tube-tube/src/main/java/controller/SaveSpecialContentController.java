package controller;

import entity.Article;
import org.json.JSONObject;
import util.StringUtill;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class SaveSpecialContentController extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(SaveSpecialContentController.class.getSimpleName());

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String contentString = StringUtill.convertInputStreamToString(req.getInputStream());

        JSONObject jsonObject = new JSONObject(contentString);
        String url = jsonObject.getString("url");
        String title = jsonObject.getString("title");
        String description = jsonObject.getString("description");
        String content = jsonObject.getString("content");

        Article article = Article.Builder.anArticle()
                .withUrl(url)
                .withDescription(description)
                .withContent(content)
                .build();
        ofy().save().entity(article).now();
        resp.getWriter().println("Okie");
    }
}
