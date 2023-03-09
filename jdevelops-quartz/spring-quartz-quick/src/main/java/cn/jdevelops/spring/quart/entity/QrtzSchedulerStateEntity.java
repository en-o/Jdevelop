package cn.jdevelops.spring.quart.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 *
 * @author tnnn
 * @version V1.0
 * @date 2023-3-7
 */
@Entity
@Table(name = "qrtz_scheduler_state")
@Getter
@Setter
@ToString
public class QrtzSchedulerStateEntity  implements Serializable,Cloneable{


    /** 调度器名  */
   @Id
    private  String  schedName ;


    /** 实例名 */
    @Id
    private  String  instanceName ;


    /** 最后检查时间 */
    private  Long  lastCheckinTime ;


    /** 检查时间间隔  */
    private  Long  checkinInterval ;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QrtzSchedulerStateEntity that = (QrtzSchedulerStateEntity) o;
        return Objects.equals(schedName, that.schedName) && Objects.equals(instanceName, that.instanceName) && Objects.equals(lastCheckinTime, that.lastCheckinTime) && Objects.equals(checkinInterval, that.checkinInterval);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schedName, instanceName, lastCheckinTime, checkinInterval);
    }
}