package com.zsl0.util.business.cqkj;

/**
 * 格点图工具
 *
 * @author zsl0
 * created on 2022/11/24 20:45
 */
public class GridDataUtil {



    /**
     * 生成uv图片
     * @param wdGrid 风向
     * @param wsGrid 风速
     * @param uPngPath uPng存储的绝对路径
     * @param vPngPath vPng存储的绝对路径
     * @return true 生成成功，反之失败
     */
//    public static boolean getUvPng(GridDataInfo wdGrid, GridDataInfo wsGrid, String uPngPath, String vPngPath) {
//        if (Objects.isNull(wdGrid) || Objects.isNull(wsGrid) || Strings.isNullOrEmpty(uPngPath) || Strings.isNullOrEmpty(vPngPath)) {
//            log.debug("参数错误 args:[uGridDataInfo={}, vGridDataInfo={}, uPngPath={}, vPngPath={}]", wdGrid, wsGrid, uPngPath, vPngPath);
//            return false;
//        }
//
//        try {
//            double[] wd_data = wdGrid.getData();
//            double[] ws_data = wsGrid.getData();
//            double[] udata = new double[wd_data.length];
//            double[] vdata = new double[ws_data.length];
//
//            for (int i = 0; i < wd_data.length; i++) {
//                udata[i] = ws_data[i] * Math.sin(wd_data[i] / 180.0 * Math.PI + Math.PI);
//                vdata[i] = ws_data[i] * Math.cos(wd_data[i] / 180.0 * Math.PI + Math.PI);
//            }
//
//            GridDataInfo uGridDataInfo = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(wdGrid), GridDataInfo.class);
//            GridDataInfo vGridDataInfo = com.alibaba.fastjson.JSONObject.parseObject(JSON.toJSONString(wdGrid), GridDataInfo.class);
//            uGridDataInfo.setData(udata);
//            vGridDataInfo.setData(vdata);
//            PngDataInfo uPngDataInfo = uGridDataInfo.toPngDataInfo();
//            PngDataInfo vPngDataInfo = vGridDataInfo.toPngDataInfo();
//            uPngDataInfo.toImage(uPngPath);
//            vPngDataInfo.toImage(vPngPath);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//
//        return true;
//    }

}
