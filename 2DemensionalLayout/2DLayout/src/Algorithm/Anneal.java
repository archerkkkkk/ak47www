package Algorithm;


import java.io.IOException;
import java.util.ArrayList;


/**
 * 采用模拟退火算法实现更新
 * 使用率(useRate)：所生产小矩形数量与所有矩形数量的百分比
 * 排样率(layoutRate)：所利用空间占总空间的百分比
 * 思路：
 * 1、先 初始化解，将初始解赋值给最佳解，定义新状态解
 * 2、打乱矩形表顺序，内循环模拟退火
 * 3、
 *
 * @author Archer
 */
public class Anneal {
    private double currentTemperature = 5000;
    private Data current;

    /**
     * 初始化
     *
     * @throws IOException IO流抛出异常
     */

    public void initData(double width, double height, int num, String filePath) throws IOException {
        current = new Data(width, height, num);
        //"D:\\桌面\\2DemensionalLayout\\111.txt"
        current.readData(filePath);
        current.layout();
    }

    /**
     * @param layoutRate    排样率
     * @param newLayoutRate 新排样率
     * @param temperature   温度
     * @return 返回可行性值
     */
    private double acceptanceProbability(double layoutRate, double newLayoutRate, double temperature) {
        // 可接受的值，更小距离置1.0，必大于Math.random()
        // 排样率：所利用空间占总空间的百分比
        if (newLayoutRate > layoutRate) {
            return 1.0;
        }
        // 重新计算一个值来探寻更优解
        return Math.exp((layoutRate - newLayoutRate) / temperature);
    }

    /**
     * 要返回一个best的Data对象
     * 在此之前一定要先执行初始化current的操作
     *
     * @throws IOException IO异常
     */
    public Data anneal() throws IOException {
        //获取矩形表而不是排样表获取更多可能的变异解
        ArrayList<MyRectangle> rectangleArrayList = current.getRectangleTale();
        //想将当前的初始解赋值给bestSolution
        Data bestSolution = new Data(current);
        //将矩形表打乱再赋值给newSolution
        Data newSolution = new Data(current);
        //设置最低温度
        double minTemperature = 0.1;
        while (currentTemperature > minTemperature) {
            //内循环次数
            double internalLoop = 1000;//(1000)
            //更新获取下一状态
            newSolution.generateNeighourTour();
            for (int i = 0; i < internalLoop; i++) {
                //计算排样率
                double currentEnergy = bestSolution.calculateLayoutRate();
                double newEnergy = newSolution.calculateLayoutRate();
                //因为要找大排样率，和TSP里找小路径长度是相反的
                if (acceptanceProbability(currentEnergy, newEnergy,
                        currentTemperature) > Math.random()) {
                    current = new Data(newSolution);
                }
                if (current.calculateLayoutRate() > bestSolution.calculateLayoutRate()) {
                    bestSolution = new Data(current);
                }
            }

            //冷却速率
            double coolingRate = 0.01;
            //每一状态更新温度计算
            currentTemperature *= 1 - coolingRate;
        }
        bestSolution.printLayoutRate();
        bestSolution.printUseRate();
        return bestSolution;
    }
}
