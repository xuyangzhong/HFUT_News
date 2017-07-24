package com.zxy.crawl.impl;

import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.zxy.crawl.dao.Dao;
import com.zxy.crawl.model.News;
import com.zxy.crawl.util.Net;
import com.zxy.crawl.util.Regex;
/**
 * 处理器，发现新的Url，抛弃旧的Url
 * @author tinytail
 *
 */
public class Handle implements Runnable{
	String html=null;
	String url=null;
	String encoding="utf-8";//网页默认编码为UTF-8
	Net net=null;
	Regex regex=null;
	static Injector inj=null;
	
	public Handle(Injector inj,Regex regex){
		this.regex=regex;
		url=inj.getUrl();
		net=new Net();
		initNet();
	}
	
	public Handle(Regex regex){
		this.regex=regex;
		url=inj.getUrl();
		net=new Net();
		initNet();
	}
	
	public Handle(Injector inj,Regex regex,String encoding){
		this.encoding=encoding;
		this.regex=regex;
		url=inj.getUrl();
		net=new Net();
		initNet();
	}
	
	public void initNet(){
		net.setUrl(url);
		net.setEncoding(encoding);
	}
	
	public void addRegex(String rule){
		if(rule.equals("")||rule==null){
			System.out.println("处理器中添加正则失败！");
			return;
		}
		regex.addRule(rule);
	}
	
	//处理的主要函数
	@Override
	public synchronized void run(){
		ArrayList<String> tempUrl=new ArrayList<String>();
		if(!net.isOk()){
			System.out.println("queue已空！");
			return;
		}
		html=net.getHtmlResourceByURL();
		if(html==null){
			return;
		}
		Document doc=Jsoup.parse(html);
		net.relativeToAbsolute(doc);
		Elements eles=doc.select("a[href]");
		for(Element ele : eles){
			String str=ele.attr("href");
			if(regex.satisfy(str)){
				tempUrl.add(str);
			}
		}
		if (Pattern.matches("http://news.hfut.edu.cn/show-.*html",url)) {
		 String title = doc.select("div[id=Article]>h2").first().text();
	     String content = doc.select("div#artibody").text();
	     String time = doc.select("div[id=Article]>h2>span").text().substring(5, 15);

	     News news=new News();
	     news.setTitle(title);
	     news.setTime(time);
	     news.setUrl(url);
	     news.setContent(content);
	     Dao dao=new Dao();
	     dao.addTo(news);
//	     System.out.println("URL:\n" + url);
//	     System.out.println("title:\n" + title);
//	     System.out.println("time:\n" + time);
//	     System.out.println("content:\n" + content);
		}
		inj.addUsefulUrl(tempUrl);
		inj.addUselessUrl(url);
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//设置爬取间隔0.5s
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Net getNet() {
		return net;
	}

	public void setNet(Net net) {
		this.net = net;
	}

	public static Injector getInj() {
		return inj;
	}

	public static void setInj(Injector inj) {
		Handle.inj = inj;
	}

	public Regex getRegex() {
		return regex;
	}

	public void setRegex(Regex regex) {
		this.regex = regex;
	}
}



