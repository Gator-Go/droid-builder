
package ppp.ppp.ppp.alert;

import java.util.Date;

/**
 * Lllll
 *
 * This is a simple POJO (XxxxxAlert) that represents an alert/event
 * to be sent to the server.
 * It is a data container for alert information, with standard getters
 * and setters.
 *
 * @author Aaaaa
 * @author <a href="mailto:aaaaa@ddddd">Aaaaa</a>
 * @version 1.0
 * @version $Id$
 */

public class XxxxxAlert {

    private Long deviceId;

    private String xxxxxAlertType;

    private String xxxxxAlertSource;

    private String xxxxxAlertMessage;

    private Date occurredAt;


    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public String getXxxxxAlertType() { return xxxxxAlertType; }
    public void setXxxxxAlertType(String xxxxxAlertType) { this.xxxxxAlertType = xxxxxAlertType; }

    public String getXxxxxAlertSource() { return xxxxxAlertSource; }
    public void setXxxxxAlertSource(String xxxxxAlertSource) { this.xxxxxAlertSource = xxxxxAlertSource; }

    public String getXxxxxAlertMessage() { return xxxxxAlertMessage; }
    public void setXxxxxAlertMessage(String xxxxxAlertMessage) { this.xxxxxAlertMessage = xxxxxAlertMessage; }

    public Date getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Date occurredAt) { this.occurredAt = occurredAt; }
   
}
