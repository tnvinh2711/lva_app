package com.lva.shop.api;


import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Product;
import com.lva.shop.ui.main.model.Tutorial;

public class ZipRequest {
    private Knowledge responseKnowledge;
    private Tutorial responseTutorial;
    private News responseNews;
    private Product responseProduct;

    public ZipRequest(Knowledge responseKnowledge, Tutorial responseTutorial, News responseNews, Product responseProduct) {
        this.responseKnowledge = responseKnowledge;
        this.responseTutorial = responseTutorial;
        this.responseNews = responseNews;
        this.responseProduct = responseProduct;
    }

    public Knowledge getResponseKnowledge() {
        return responseKnowledge;
    }

    public void setResponseKnowledge(Knowledge responseKnowledge) {
        this.responseKnowledge = responseKnowledge;
    }

    public Tutorial getResponseTutorial() {
        return responseTutorial;
    }

    public void setResponseTutorial(Tutorial responseTutorial) {
        this.responseTutorial = responseTutorial;
    }

    public News getResponseNews() {
        return responseNews;
    }

    public void setResponseNews(News responseNews) {
        this.responseNews = responseNews;
    }

    public Product getResponseProduct() {
        return responseProduct;
    }

    public void setResponseProduct(Product responseProduct) {
        this.responseProduct = responseProduct;
    }
}
