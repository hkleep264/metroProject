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
public class SignUp extends JFrame{
	
	private static MemberDAO dao = null;
	
	/**
	 * 회원가입 화면 생성
	 */
	public SignUp(){
		
		JPanel panel = new JPanel();
		
		JLabel name = new JLabel("이름 : ");
		
		JLabel label = new JLabel("ID : ");

		JLabel pswrd = new JLabel("PassWord : ");
		
		JTextField txtName= new JTextField(30);

		JTextField txtID= new JTextField(30);

		JPasswordField txtPass = new JPasswordField(30);

		JButton logBtn = new JButton("회원가입");
		JButton exitBtn = new JButton("취소");
		try {
			dao = MemberDAO.getInstance();
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		
		panel.add(label);
		panel.add(txtID);
		panel.add(pswrd);
		panel.add(txtPass);
		panel.add(name);
		panel.add(txtName);
		panel.add(logBtn);
		panel.add(exitBtn);

		logBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 회원가입 버튼 이벤트
				MemberVo vo = new MemberVo();
				vo.setId(txtID.getText());
				vo.setPw(txtPass.getText());
				vo.setName(txtName.getText());
				
				try {
					boolean result = dao.signUp(vo);
					if(result) {
						//성공
						JOptionPane.showMessageDialog(null, "성공");
						dispose(); //현재 화면 닫기
					}else {
						//실패
						JOptionPane.showMessageDialog(null, "중복된 아이디 입니다.");
					}
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
				
			}
		});
		
		exitBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//닫기 버튼 이벤트
				dispose();
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
