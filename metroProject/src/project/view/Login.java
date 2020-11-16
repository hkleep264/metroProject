package project.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import project.dao.MemberDAO;
import project.vo.MemberVo;

public class Login extends JFrame{

	private static MemberDAO dao = null;
	
	/**
	 * �α��� ȭ�� ����
	 */
	public Login() {

		JPanel panel = new JPanel();
		
		JPanel panelTemp1 = new JPanel();
		JPanel panelTemp2 = new JPanel();

		JLabel label = new JLabel("ID : ");

		JLabel pswrd = new JLabel("PassWord : ");

		JTextField txtID= new JTextField(30);

		JPasswordField txtPass = new JPasswordField(30);

		JButton logBtn = new JButton("Log in");
		JButton signUpBtn = new JButton("signUpBtn");
		try {
			dao = MemberDAO.getInstance();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}


		panelTemp1.add(label);
		panelTemp1.add(txtID);
		panelTemp2.add(pswrd);
		panelTemp2.add(txtPass);
		panel.add(panelTemp1);
		panel.add(panelTemp2);
		panel.add(logBtn);
		panel.add(signUpBtn);

		logBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// �α��� ��ư �׼� �̺�Ʈ
				MemberVo vo = new MemberVo();
				vo.setId(txtID.getText());
				vo.setPw(txtPass.getText());
				
				try {
					boolean result = dao.login(vo);
					if(result) {
						//������ ����ö ��ȸ ȭ��
						Metro metro = new Metro();
						
					}else {
						JOptionPane.showMessageDialog(null, "ID�� PW�� �ٸ��ϴ�.");
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				
				
					
				
			}
		});
		
		signUpBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//ȸ������ ��ư �̺�Ʈ
				
				SignUp page = new SignUp();
				
			}
		});

		add(panel);
		setVisible(true);

		setSize( 600 , 600);

		setLocationRelativeTo(null);

		setResizable(false);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
