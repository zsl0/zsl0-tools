package com.zsl0.util.business.cqkj;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据检验计算工具
 *
 * @author zsl0
 * created on 2022/11/24 20:43
 */
public class VerifyUtil {

    /**
     * 偏差值（四舍五入，保留8位小时）
     * 公式：预测值-实际值
     */
    public static BigDecimal deviation(BigDecimal prediction, BigDecimal real) {
        return prediction.subtract(real);
    }

    /**
     * 偏差率（四舍五入，保留两位小时）
     * 公式：偏差值 / 实际值 * 100%
     */
    public static BigDecimal deviationRatio(BigDecimal prediction, BigDecimal real) {
        return deviation(prediction, real)
                .divide(real, 8, RoundingMode.HALF_UP);
    }

    /**
     * 平均准确率（注意：prediction、real数据值，时刻要保持一直）
     * 入参为预测、实际值
     * 公式：（1 - 根号下 所有时刻偏差率平方求和 除以时刻个数） * 100%
     */
    public static BigDecimal precisionRatio(List<BigDecimal> predictions, List<BigDecimal> reals) {
        List<BigDecimal> deviationRatios = new ArrayList<>();

        // 计算偏差率
        for (int i = 0; i < predictions.size(); i++) {
            BigDecimal prediction = predictions.get(i);
            BigDecimal real = reals.get(i);

            BigDecimal deviationRatio = deviationRatio(prediction, real);
            deviationRatios.add(deviationRatio);
        }

        // 计算准确率
        return precisionRatio(deviationRatios);
    }

    /**
     * 平均准确率
     * 入参为偏差率
     */
    public static BigDecimal precisionRatio(List<BigDecimal> deviationRatios) {
        if (deviationRatios.isEmpty()) {
            return null;
        }

        // 计算平均偏差率  求偏差率平方后平均
        double avg = deviationRatios.stream().mapToDouble(BigDecimal::doubleValue).map(a -> Math.pow(a, 2)).average().orElse(-1);
        // 计算准确率
        return BigDecimal.ONE.subtract(BigDecimal.valueOf(Math.sqrt(BigDecimal.valueOf(avg).doubleValue())));
    }

    /**
     * 绝对误差
     * 公式：| prediction - real |
     */
    public static BigDecimal absoluteError(BigDecimal prediction, BigDecimal real) {
        return prediction.subtract(real).abs().setScale(2, RoundingMode.HALF_UP);
    }


}
