package com.javarush.task.task28.task2810.view;

import com.javarush.task.task28.task2810.Controller;
import com.javarush.task.task28.task2810.vo.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.List;

public class HtmlView implements View{

    private final String filePath = "./4.JavaCollections/src/" + this.getClass().getPackage().getName().replaceAll("[.]", "/") + "/vacancies.html";

    private Controller controller;
    @Override
    public void update(List<Vacancy> vacancies) {
        try {
            String newContent = getUpdatedFileContent(vacancies);
            updateFile(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getUpdatedFileContent(List<Vacancy> vacancies) {
        try {
            Document document = getDocument();
            Elements templateHidden = document.getElementsByClass("template");
            Element template = templateHidden.clone().removeAttr("style").removeClass("template").get(0);
            Elements prevVacancies = document.getElementsByClass("vacancy");
            for (Element redundant : prevVacancies) {
                if (!redundant.hasClass("template")) {
                    redundant.remove();
                }
            }
            for (Vacancy vacancy : vacancies) {
                Element vacancyElement = template.clone();
                Element city = vacancyElement.getElementsByClass("city").get(0);
                city.appendText(vacancy.getCity());
                Element companyName = vacancyElement.getElementsByClass("companyName").get(0);
                companyName.appendText(vacancy.getCompanyName());
                Element salary = vacancyElement.getElementsByClass("salary").get(0);
                salary.appendText(vacancy.getSalary());
                Element vacancyLink = vacancyElement.getElementsByAttribute("href").get(0);
                vacancyLink.appendText(vacancy.getTitle());
                vacancyLink.attr("href", vacancy.getUrl());
                templateHidden.before(vacancyElement.outerHtml());
            }
            return document.html();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Some exception occurred";
    }

    private void updateFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected Document getDocument() throws IOException {
        Document document = Jsoup.parse(new File(filePath), "UTF-8");
        return document;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void userCitySelectEmulationMethod() {
        controller.onCitySelect("Odessa");
    }
}
