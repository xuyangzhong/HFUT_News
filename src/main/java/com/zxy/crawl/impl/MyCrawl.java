package com.zxy.crawl.impl;

import com.zxy.crawl.util.Regex;

public class MyCrawl {
	public static void main(String[]args){
		Injector inj=new Injector();
		inj.addUsefulUrl("http://news.hfut.edu.cn/");
		Regex reg=new Regex();
		reg.addRule("http://news.hfut.edu.cn/list-.*html");
		reg.addRule("http://news.hfut.edu.cn/show-.*html");
		while(!inj.isEmpty()&&inj.getUselessUrlNum()<=4000){
			System.out.println("-----------------------------");
			System.out.println("队列中等待爬取Url数量:"+inj.getUsefulUrlNum());
			System.out.println("已经完成爬取的Url数量:"+inj.getUselessUrlNum());
			System.out.println("-----------------------------");
			
			Handle.setInj(inj);
			
			Thread []t=new Thread[100];
			for(int i=0;i<t.length;i++){
				Handle handle=new Handle(reg);
				t[i]=new Thread(handle);
				t[i].start();
			}
			
			//留给主程序一秒钟时间，防止上列第一个线程执行时inj已空立即退出的情况
			try {				
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			
		}
		System.out.println("------------end!-------------");
	}
}







