package project.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import project.api.MetroAPI;
import project.dao.MetroDAO;
import project.vo.MetroVo;

public class Metro  extends JFrame{
	
	private static MetroDAO dao = null;
	private static MetroAPI api = null;
	
	/**
	 * 지하철 역 조회 화면 생성
	 */
	public Metro() {
		
		try {
			dao = MetroDAO.getInstance();
			api = MetroAPI.getInstance();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		ArrayList<MetroVo> lineList = null;
		int startMetroListCnt = 0;
		ArrayList<MetroVo> startMetroList = null;
		int endMetroListCnt = 0;
		ArrayList<MetroVo> endMetroList = null;
		int lineCnt = 0;
		try {
			lineList = dao.getLineList();
			lineCnt = dao.getLineListCnt();
			startMetroListCnt = dao.getStationListCnt(lineList.get(0).getLine_no());
			startMetroList = dao.getStationList(lineList.get(0).getLine_no());
			endMetroListCnt = dao.getStationListCnt(lineList.get(0).getLine_no());
			endMetroList = dao.getStationList(lineList.get(0).getLine_no());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String [] line = new String [lineCnt];
		String [] sName = new String[startMetroListCnt];
		String [] eName = new String[endMetroListCnt];
		
		for(int i=0; i<lineList.size(); i++) {
			line[i] = lineList.get(i).getLine_no();
		}
		
		for(int i=0; i<startMetroList.size(); i++) {
			sName[i] = startMetroList.get(i).getStation_nm_ko();
		}
		
		for(int i=0; i<endMetroList.size(); i++) {
			eName[i] = endMetroList.get(i).getStation_nm_ko();
		}
		
		JPanel panel = new JPanel();
		
         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel panelSelect = new JPanel();
		JPanel panelBtn = new JPanel();
		
		JComboBox sLineCombo = new JComboBox(line);  

		JComboBox sStationCombo = new JComboBox(sName);
		sStationCombo.setPreferredSize(new Dimension(100,30));


		
		
		JComboBox eLineCombo = new JComboBox(line);
		JComboBox eStationCombo = new JComboBox(eName);
		eStationCombo.setPreferredSize(new Dimension(150,30));




		JButton searchBtn = new JButton("검색");
		JButton addBtn = new JButton("인원 추가");
		
		
		sLineCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//호선 변경 시 동작 이벤트
				JComboBox cb = (JComboBox)e.getSource();
				
				String item = cb.getSelectedItem().toString();
				ArrayList<MetroVo> startMetroListTemp = null;
				int startMetroListCnt = 0;
				//지하철 역가져오기
				try {
					startMetroListCnt = dao.getStationListCnt(item);
					startMetroListTemp = dao.getStationList(item);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String [] sNameTemp = new String[startMetroListCnt];
				
				for(int i=0; i<startMetroListTemp.size(); i++) {
					sNameTemp[i] = startMetroListTemp.get(i).getStation_nm_ko();
				}
				
				DefaultComboBoxModel model = new DefaultComboBoxModel( sNameTemp );
				sStationCombo.setModel( model );
				
			}
		});
		
		eLineCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//호선 변경 시 동작 이벤트
				JComboBox cb = (JComboBox)e.getSource();
				
				String item = cb.getSelectedItem().toString();
				ArrayList<MetroVo> endMetroListTemp = null;
				int endMetroListCnt = 0;
				//지하철 역가져오기
				try {
					endMetroListCnt = dao.getStationListCnt(item);
					endMetroListTemp = dao.getStationList(item);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				String [] eNameTemp = new String[endMetroListCnt];
				
				for(int i=0; i<endMetroListTemp.size(); i++) {
					eNameTemp[i] = endMetroListTemp.get(i).getStation_nm_ko();
				}
				
				DefaultComboBoxModel model = new DefaultComboBoxModel( eNameTemp );
				eStationCombo.setModel( model );
			}
		});
		
		searchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//검색 버튼 클릭 이벤트

				int i =0;
				int j =0;
				int componentCount = panelSelect.getComponentCount()/2;
				
				String [] lineSel = new String[componentCount];
				String [] NameSel = new String[componentCount];
				
				ArrayList<HashMap<String,Object>> map = new ArrayList<HashMap<String,Object>>();
				
				for (Component component : panelSelect.getComponents()) {
					if (component instanceof JComboBox) {
						if( (i > 0) && (i % 2 !=0) ) {
							NameSel[j] = ((JComboBox) component).getSelectedItem().toString();
							j++;
						}else {
							lineSel[j] = ((JComboBox) component).getSelectedItem().toString();
						}
						i++;
					}
				}
				for(int l=0; l<componentCount; l++) {
					MetroVo metroVo = new MetroVo();
					try {
						metroVo = dao.getStationFrCode(NameSel[l], lineSel[l]);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					HashMap<String,Object> check = new HashMap<String,Object>();
					check.put("x", metroVo.getX());
					check.put("y", metroVo.getY());
					
					map.add(check);
				}
				
				String result = api.getMidPoint(map);
				
				JOptionPane.showMessageDialog(null, "중간지점: "+result);
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
			
				//인원 추가 버튼 이벤트
				int lineCnt = 0;
				ArrayList<MetroVo> lineList = null;
				int MetroListCnt = 0;
				ArrayList<MetroVo> MetroList = null;
				
				try {
					lineCnt = dao.getLineListCnt();
					lineList = dao.getLineList();
					MetroListCnt = dao.getStationListCnt(lineList.get(0).getLine_no());
					MetroList = dao.getStationList(lineList.get(0).getLine_no());
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				String [] line = new String [lineCnt];
				String [] metroName = new String[MetroListCnt];
				
				for(int i=0; i<lineList.size(); i++) {
					line[i] = lineList.get(i).getLine_no();
				}
				
				for(int i=0; i<MetroList.size(); i++) {
					metroName[i] = MetroList.get(i).getStation_nm_ko();
				}
				
				JComboBox lineCombo = new JComboBox(line);
				JComboBox metroCombo = new JComboBox(metroName);  
				
				lineCombo.setPreferredSize(new Dimension(100,30));
				metroCombo.setPreferredSize(new Dimension(150,30));
				
				//이벤트 추가
				lineCombo.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//호선 변경 이벤트
						JComboBox cb = (JComboBox)e.getSource();
						
						String item = cb.getSelectedItem().toString();
						ArrayList<MetroVo> MetroListTemp = null;
						int MetroListCnt = 0;
						//지하철 역가져오기
						try {
							MetroListCnt = dao.getStationListCnt(item);
							MetroListTemp = dao.getStationList(item);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						String [] eNameTemp = new String[MetroListCnt];
						
						for(int i=0; i<MetroListTemp.size(); i++) {
							eNameTemp[i] = MetroListTemp.get(i).getStation_nm_ko();
						}
						
						DefaultComboBoxModel model = new DefaultComboBoxModel( eNameTemp );
						metroCombo.setModel( model );
					}
				});
				
				
				panelSelect.add(lineCombo);
				panelSelect.add(metroCombo);
				
				panel.revalidate();
				panel.repaint();
				
			}
		});
		

		

		
		panelSelect.add(sLineCombo);
		panelSelect.add(sStationCombo);
		
		panelSelect.add(eLineCombo);
		panelSelect.add(eStationCombo);
		
		panelBtn.add(addBtn);
		panelBtn.add(searchBtn);
		
		panel.add(panelSelect);
		
		
		panel.add(panelBtn);
		
		
		add(panel);
		setVisible(true);

		setSize( 600 , 600);

		setLocationRelativeTo(null);

		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

}
