package com.outbrain.blog.app;

/**
 * User: maromg
 * Date: 30/06/2014
 */
public class BlogApplicationMain {

    public static void main(String[] args) {
        new BlogApplicationLoader().load();
        new DemoDataPopulator().populate();
    }


}
