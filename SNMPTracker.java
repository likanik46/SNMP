package SNMP;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTable;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SNMPTracker{
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTable table = new JTable(7, 4);
		table.setValueAt("x", 0, 0);
		table.setValueAt("Broj dolaznih SNMP paketa", 1, 0);
		table.setValueAt("Broj odlaznih SNMP paketa", 2, 0);
		table.setValueAt("Broj get zahteva", 3, 0);
		table.setValueAt("Broj set zahteva", 4, 0);
		table.setValueAt("Broj generisanih trap-ova", 5, 0);
		table.setValueAt("Broj neispravnih community vrednosti", 6, 0);
		
		
		table.setValueAt("R1", 0, 1);
		table.setValueAt("R2", 0, 2);
		table.setValueAt("R3", 0, 3);
		
		frame.add(table);
		frame.setSize(1000, 200);
		frame.setVisible(true);
		
		
		String[] routerAddresses = {"192.168.10.1/161", "192.168.20.1/161", "192.168.30.1/161"};
		String community = "si2019";
		
	
		
		Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
		snmp.listen();
		
		
		CommunityTarget[] communityTargets = new CommunityTarget[3];
		for (int i = 0; i < 3; i++) {
			communityTargets[i] = new CommunityTarget(new UdpAddress(routerAddresses[i]), new OctetString(community));
			communityTargets[i].setVersion(SnmpConstants.version1);
		}
	
		
		PDU pdu = new PDU();
		pdu.setType(PDU.GET);
		pdu.addOID(new VariableBinding(new OID(".1.3.6.1.2.1.11.1.0")));
		pdu.addOID(new VariableBinding(new OID(".1.3.6.1.2.1.11.2.0")));
		pdu.addOID(new VariableBinding(new OID(".1.3.6.1.2.1.11.15.0")));
		pdu.addOID(new VariableBinding(new OID(".1.3.6.1.2.1.11.17.0")));
		pdu.addOID(new VariableBinding(new OID(".1.3.6.1.2.1.11.29.0")));
		pdu.addOID(new VariableBinding(new OID(".1.3.6.1.2.1.11.4.0")));
		
		
		while (true) {
			ResponseEvent[] ResponseEvents = new ResponseEvent[3];
			for (int i = 0; i < 3; i++) {
				ResponseEvents[i] = snmp.get(pdu, communityTargets[i]);
			}
			
			PDU[] responsePDUs = new PDU[3];
			for (int i = 0; i < 3; i++) {
				responsePDUs[i] = ResponseEvents[i].getResponse();
			}
			
		
			for (int i = 0; i < 3; i++) {
				for (int j = 0 ; j < 6; j++) {
					if (responsePDUs[i] != null) {
						table.setValueAt(responsePDUs[i].get(j).getVariable(), j + 1, i + 1);
					}
				}
			}
			
			Thread.sleep(2000);
			System.out.println("\n\n");
		}
	}
	
}
match ip address 101
set community 3:200
route-map COMMUNITY permit 10
exit*/
