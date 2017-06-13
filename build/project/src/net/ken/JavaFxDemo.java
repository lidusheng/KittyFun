package net.ken;

import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.StringConverter;
import net.bean.AMessage;
import org.htmlparser.nodes.TagNode;

/**
 *
 * @author LiDusheng
 */
public class JavaFxDemo extends Application {

    //WebClient one
    private static WebClient webclient;
    //首页
    private static HtmlPage page;
    //搜索框
    private static HtmlTextInput textField;
    //搜索按钮
    private static HtmlButton button;
    //托盘图标
    private TrayIcon trayIcon;

    @Override
    public void start(Stage ps) throws Exception {
        ps.setTitle("BTKitty V1.0.1");
        ps.getIcons().add(new Image(getClass().getResourceAsStream("logoICO.png")));
        enableTray(ps);
        Parent helloKitty = FXMLLoader.load(getClass().getResource("HelloKitty.fxml"));
        TabPane bt_tabPane = (TabPane) helloKitty.lookup("#bt_tabPane");
        Label bt_labelTT = (Label) helloKitty.lookup("#bt_labelTT");
        bt_labelTT.setWrapText(true);
        TextField searchfield = (TextField) helloKitty.lookup("#searchfield");
        searchfield.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {
                    String text = searchfield.getText();
                    if (text.trim().equals("")) {
                        return;
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 获取OneTab
                                Parent OneTab = FXMLLoader.load(getClass().getResource("OneTab.fxml"));
                                Tab oneTab = new Tab(text, OneTab);
                                AnchorPane ap = (AnchorPane) OneTab.lookup("#ap");
                                if (TODOSearch(text, ap, bt_labelTT)) {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            bt_tabPane.getTabs().add(oneTab);
                                            bt_tabPane.getSelectionModel().select(oneTab);
                                        }
                                    });
                                }
                            } catch (IOException e) {
                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        bt_labelTT.setTextFill(Color.RED);
                                        bt_labelTT.setFont(new Font("Arial", 14));
                                        bt_labelTT.setText("网络未连接,请稍后再试。");
                                    }
                                });
                            }

                        }
                    }).start();
                }
            }
        });
        Scene scene = new Scene(helloKitty);
        scene.getStylesheets().add("styles/styles.css");
        ps.setScene(scene);
        ps.show();
        ps.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent we) {
                ps.hide();
                we.consume();
            }
        });
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    page = webclient.getPage("http://btkitty.kim");
                    final HtmlForm form = page.getFormByName("search");
                    // 获取提交按扭
                    button = (HtmlButton) form.getElementsByAttribute("button", "type", "submit").get(0);
                    // 一会得输入的
                    textField = form.getInputByName("keyword");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            bt_labelTT.setTextFill(Color.GREEN);
                            bt_labelTT.setFont(new Font("Arial", 14));
                            bt_labelTT.setText("正常");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            bt_labelTT.setTextFill(Color.RED);
                            bt_labelTT.setFont(new Font("Arial", 14));
                            bt_labelTT.setText("网络未连接,请稍后再试。");
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 设置托盘
     *
     * @param stage
     */
    private void enableTray(final Stage stage) {
        String ExitStr = "EXIT";
        String ShowStr = "SHOW";
        PopupMenu popupMenu = new PopupMenu();
        java.awt.MenuItem openItem = new java.awt.MenuItem(ShowStr);
        java.awt.MenuItem quitItem = new java.awt.MenuItem(ExitStr);
        Platform.setImplicitExit(false);
        ActionListener acl = new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                java.awt.MenuItem item = (java.awt.MenuItem) e.getSource();
                if (item.getLabel().equals(ExitStr)) {
                    SystemTray.getSystemTray().remove(trayIcon);
                    Platform.exit();
                    return;
                }
                if (item.getLabel().equals(ShowStr)) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            }
        };
        openItem.addActionListener(acl);
        quitItem.addActionListener(acl);
        popupMenu.add(openItem);
        popupMenu.add(quitItem);
        try {
            SystemTray tray = SystemTray.getSystemTray();
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("logoICO.png"));
            trayIcon = new TrayIcon(image, "小小程序员 自己动手 丰衣足食", popupMenu);
            trayIcon.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                stage.show();
                            }
                        });
                    }
                }
            });
            tray.add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean TODOSearch(String s, AnchorPane ap, Label log) {
        VBox outerBox = (VBox) ap.lookup("#page_main");
        final HtmlPage pp;// 检索后的网页
        final Parser parser;
        try {
            if (page == null) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        log.setTextFill(Color.RED);
                        log.setFont(new Font("Arial", 14));
                        log.setText("网络未连接,请稍后再试。");
                    }
                });
                return false;
            }
            textField.setValueAttribute(s);
            // 点击提交表单
            pp = button.click();
            String asXml = pp.asXml();
            parser = new Parser(asXml);
            AndFilter andFilter1 = new AndFilter(
                    new NodeFilter[]{new TagNameFilter("div"), new CssSelectorNodeFilter(".bar")});
            // 得到所有经过过滤的标签，结果为NodeList
            NodeList divl = parser.extractAllNodesThatMatch(andFilter1);
            ///html/body/div[1]/div[3]/dl[1]/dd
            if (divl.size() != 1) {
                parser.reset();
                AndFilter andFilter2 = new AndFilter(
                        new NodeFilter[]{new TagNameFilter("div"), new CssSelectorNodeFilter(".content")});
                // 得到所有经过过滤的标签，结果为NodeList
                NodeList dl = parser.extractAllNodesThatMatch(andFilter2);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        log.setTextFill(Color.RED);
                        log.setFont(new Font("Arial", 14));
                        log.setText(dl.elementAt(0).getChildren().elementAt(1).getChildren().elementAt(3).toHtml().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "").replaceAll("  ",
                                "").replaceAll("<dd>|</dd>|<strong>|</strong>", ""));
                    }
                });
                return false;
            }
            Div div = (Div) divl.elementAt(0);
            String stringText = div.getStringText();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    log.setTextFill(Color.BLUE);
                    log.setFont(new Font("Arial", 14));
                    log.setText(stringText.replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "").replaceAll("  ",
                            ""));
                }
            });
            parser.reset();
            // 获得页数
            AndFilter andFilter2 = new AndFilter(
                    new NodeFilter[]{new TagNameFilter("div"), new CssSelectorNodeFilter(".pagination")});
            // 得到所有经过过滤的标签，结果为NodeList
            NodeList pagedivl = parser.extractAllNodesThatMatch(andFilter2);
            NodeList spanl = pagedivl.extractAllNodesThatMatch(new TagNameFilter("span"), true);
            Span t = (Span) spanl.elementAt(0);
            int ys = Integer.parseInt(t.getStringText().replace("Pages", "").trim());

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    // 组织页码
                    outerBox.setAlignment(Pos.CENTER);
                    Pagination pagination = new Pagination(ys);
                    pagination.setPageFactory((Integer pageIndex) -> createAnimalPage(pageIndex, pp, log));
                    pagination.setStyle("-fx-border-color:green;");
//                    Button styleButton = new Button("Toggle pagination style");
//                    styleButton.setOnAction((ActionEvent me) -> {
//                        if (!pagination.getStyleClass().contains(Pagination.STYLE_CLASS_BULLET)) {
//                            pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
//                        } else {
//                            pagination.getStyleClass().remove(Pagination.STYLE_CLASS_BULLET);
//                        }
//                    });
//                    outerBox.getChildren().addAll(pagination, styleButton);
                    pagination.setMaxPageIndicatorCount(20);
                    pagination.setCache(true);
                    pagination.setCacheHint(CacheHint.SPEED);
                    outerBox.getChildren().addAll(pagination);
                    outerBox.setAlignment(Pos.TOP_CENTER);
                    ap.setBottomAnchor(outerBox, 0.0);
                    ap.setLeftAnchor(outerBox, 0.0);
                    ap.setRightAnchor(outerBox, 0.0);
                    ap.setTopAnchor(outerBox, 0.0);

                }
            });
            return true;
        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    log.setTextFill(Color.RED);
                    log.setFont(new Font("Arial", 14));
                    log.setText("获取数据失败");
                }
            });
            // e.printStackTrace();
            return false;
        }
    }

    private VBox createAnimalPage(int pageIndex, HtmlPage pp, Label log) {
        URL linkstr = pp.getUrl();
        VBox box = new VBox();
        final ObservableList<AMessage> data = FXCollections.observableArrayList();
        try {
            String apageurl = linkstr.toString().replace("/1", "/" + (pageIndex + 1));
            HtmlPage onepage;
            if (pageIndex == 1) {//如果是第一页直接拿过来用，节省时间
                onepage = pp;
            } else {
                onepage = (HtmlPage) webclient.getPage(apageurl);
            }
            String asXml = onepage.asXml();
            Parser parser = new Parser(asXml);
            AndFilter andFilter3 = new AndFilter(
                    new NodeFilter[]{new TagNameFilter("dl"), new CssSelectorNodeFilter(".list-con")});
            NodeList dll = parser.extractAllNodesThatMatch(andFilter3);
            for (Node n : dll.toNodeArray()) {
                // 每个n就是一个dl
                String xuhao = n.getChildren().elementAt(1).getChildren().elementAt(2).toHtml().replace(".", "").trim();
                TagNode linkTag = (TagNode) n.getChildren().elementAt(1).getChildren().elementAt(5);
                TagNode mlink = (TagNode) n.getChildren().elementAt(3).getChildren().elementAt(1).getChildren().elementAt(1);
                //dd/
//                System.out.println(n.getChildren().elementAt(3).getChildren().elementAt(3).toHtml().replaceAll(" ", ""));
                String addtime = n.getChildren().elementAt(3).getChildren().elementAt(3).toPlainTextString().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "")
                        .replaceAll("  ", "");
                String fsize = n.getChildren().elementAt(3).getChildren().elementAt(5).toPlainTextString().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "")
                        .replaceAll("  ", "");
                String fnum = n.getChildren().elementAt(3).getChildren().elementAt(7).toPlainTextString().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "")
                        .replaceAll("  ", "");
                String speed = n.getChildren().elementAt(3).getChildren().elementAt(9).toPlainTextString().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "")
                        .replaceAll("  ", "");
                String Popularity = n.getChildren().elementAt(3).getChildren().elementAt(11).toPlainTextString().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "")
                        .replaceAll("  ", "");
                String btname;
                String btn = linkTag.toPlainTextString().replaceAll("\r|\n", "").replaceAll("<b>|</b>|<i>|</i>", "")
                        .replaceAll("  ", "");
                if (btn.contains("[email protected]")) {
                    String luanqibaz = btn.substring(btn.indexOf("[email protected]"),
                            btn.lastIndexOf("]]> *///]]>") + 11);
                    btname = btn.replace(luanqibaz, "");
                } else if (btn.contains("]]> */</script>")) {
                    String luanqibaz = btn.substring(btn.indexOf("<span class="),
                            btn.lastIndexOf("]]> */</script>") + 15);
                    btname = btn.replace(luanqibaz, "");

                } else {
                    btname = btn;
                }
                data.add(new AMessage(false, btname, addtime.substring(addtime.indexOf(":") + 1, addtime.length()), fsize.substring(fsize.indexOf(":") + 1, fsize.length()), fnum.substring(fnum.indexOf(":") + 1, fnum.length()), speed.substring(speed.indexOf(":") + 1, speed.length()), Popularity.substring(Popularity.indexOf(":") + 1, Popularity.length()), mlink.getAttribute("href")));
            }
        } catch (Exception e) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    log.setTextFill(Color.RED);
                    log.setFont(new Font("Arial", 14));
                    log.setText("解析数据失败");
                }
            });
            e.printStackTrace();
        }
        StringConverter<Object> sc = new StringConverter<Object>() {
            @Override
            public String toString(Object t) {
                return t == null ? null : t.toString();
            }

            @Override
            public Object fromString(String string) {
                return string;
            }
        };
        TableColumn invitedCol = new TableColumn<>();
        invitedCol.setText("");
        invitedCol.setMinWidth(60);
        invitedCol.setCellValueFactory(new PropertyValueFactory("invited"));
        invitedCol.setCellFactory(CheckBoxTableCell.forTableColumn(invitedCol));

        TableColumn fileNameCol = new TableColumn();
        fileNameCol.setText("种子名称");
        fileNameCol.setPrefWidth(460);
        fileNameCol.setCellValueFactory(new PropertyValueFactory("fileName"));
        fileNameCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        TableColumn slShiJianCol = new TableColumn();
        slShiJianCol.setText("收录时间");
        slShiJianCol.setCellValueFactory(new PropertyValueFactory("slShiJian"));
        slShiJianCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        TableColumn wjDaXiaoCol = new TableColumn();
        wjDaXiaoCol.setText("文件大小");
        wjDaXiaoCol.setCellValueFactory(new PropertyValueFactory("wjDaXiao"));
        wjDaXiaoCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        TableColumn wjShuCol = new TableColumn();
        wjShuCol.setText("文件数");
        wjShuCol.setCellValueFactory(new PropertyValueFactory("wjShu"));
        wjShuCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        TableColumn SDCol = new TableColumn();
        SDCol.setText("速度");
        SDCol.setCellValueFactory(new PropertyValueFactory("SD"));
        SDCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        TableColumn RQCol = new TableColumn();
        RQCol.setText("人气");
        RQCol.setCellValueFactory(new PropertyValueFactory("RQ"));
        RQCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));

        TableColumn ciliLianCol = new TableColumn();
        ciliLianCol.setText("磁力链");
        ciliLianCol.setMinWidth(450);
        ciliLianCol.setCellValueFactory(new PropertyValueFactory("ciliLian"));
        ciliLianCol.setCellFactory(TextFieldTableCell.forTableColumn(sc));
        
        TableView<AMessage> table = new TableView();
        table.setPrefHeight(800);
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setItems(data);
        table.setEditable(true);
        //收录时间 : 2008-03-05 / 文件大小 : 8.33 GB / 文件数:199 / 速度 : 极快 / 人气 : 12635
        table.getColumns().addAll(invitedCol, fileNameCol, slShiJianCol, wjDaXiaoCol, wjShuCol, SDCol, RQCol, ciliLianCol);
        box.setAlignment(Pos.TOP_CENTER);
        Label desc = new Label("PAGE " + (pageIndex + 1));
        table.setOnMousePressed(new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (event.getButton() == MouseButton.SECONDARY) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            ObservableList<AMessage> getData = table.getSelectionModel().getSelectedItems();
                            for (AMessage t : getData) {
                                sb.append(t.ciliLianProperty().getValueSafe() + "\n");
                            }
                            Clipboard clipboard = Clipboard.getSystemClipboard();
                            ClipboardContent clipboardContent = new ClipboardContent();
                            clipboardContent.putString(sb.toString());
                            clipboard.setContent(clipboardContent);
                            log.setTextFill(Color.BLUE);
                            log.setFont(new Font("Arial", 14));
                            log.setText("您复制了" + getData.size() + "条磁力链接");
                        }
                    });
                }
            }
        });
        box.getChildren().addAll(table, desc);
        box.setCache(true);
        return box;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
