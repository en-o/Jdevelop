package cn.tannn.jdevelops.spi.fixture;

import cn.tannn.jdevelops.spi.JoinSPI;

/**
 * 测试SPI的实现1
 *
 * @author tn
 * @version 1
 * @date 2022-04-01 11:11
 */
@JoinSPI
public class TestSPIImpl_2 implements TestSPI{
    @Override
    public String test() {
        return "test2";
    }
}
