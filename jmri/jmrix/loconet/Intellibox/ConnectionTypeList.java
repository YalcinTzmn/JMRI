// ConnectionTypeList.java

package jmri.jmrix.loconet.Intellibox;


/**
 * Returns a list of valid lenz XpressNet Connection Types
 * <P>
 * @author      Bob Jacobsen   Copyright (C) 2010
 * @author      Kevin Dickerson    Copyright (C) 2010
 * @version	$Revision: 1.1 $
 *
 */
public class ConnectionTypeList  implements jmri.jmrix.ConnectionTypeList {

    public String[] getAvailableProtocolClasses() {
        String[] masterList = new jmri.jmrix.loconet.ConnectionTypeList().getAvailableProtocolClasses();
        
        String[] tempList = new String[masterList.length + 1];
        tempList[0] = "jmri.jmrix.loconet.Intellibox.ConnectionConfig";
        int x = 1;
        for (int i = 0; i<masterList.length; i++) {
            tempList[x] = masterList[i];
            x++;
        }
        return tempList;
    }

}

