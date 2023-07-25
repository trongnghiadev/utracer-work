/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.utrace.util;

import com.urvega.framework.util.Config;
import com.urvega.framework.util.ConvertUtil;

/**
 *
 * @author superman
 */
public class ConfigInfo {
    public static String TEMPLATE_PATH = ConvertUtil.toString(Config.getParam("web", "template"));
    public static int PORT = ConvertUtil.toInt(Config.getParam("web", "port"));
    public static String HOST = ConvertUtil.toString(Config.getParam("web", "host"));
    public static int MIN_THREAD = ConvertUtil.toInt(Config.getParam("web", "minthread"));
    public static int MAX_THREAD = ConvertUtil.toInt(Config.getParam("web", "maxthread"));
}
