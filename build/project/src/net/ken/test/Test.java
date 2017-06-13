/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ken.test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LiDusheng
 */
public class Test {

    public static void main() {
        //http://savebt.com/infohash/.html
        String a = "2EE47544C4E9DDDF4F7D212EC1C217A4C0FB05B7";
        WebClient webclient;
        webclient = new WebClient(BrowserVersion.FIREFOX_52);
        webclient.getOptions().setCssEnabled(false);
        webclient.getOptions().setJavaScriptEnabled(false);
        webclient.getOptions().setActiveXNative(false);
        webclient.getOptions().setDownloadImages(false);
        webclient.getOptions().setDoNotTrackEnabled(false);
        webclient.getOptions().setThrowExceptionOnScriptError(false);
        webclient.getOptions().setGeolocationEnabled(false);
        webclient.getOptions().setPopupBlockerEnabled(false);
        webclient.getOptions().setPrintContentOnFailingStatusCode(false);
        try {
            HtmlPage page = webclient.getPage("http://savebt.com/infohash/" + a + ".html");
            final HtmlForm form = page.getFormByName("search");
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
