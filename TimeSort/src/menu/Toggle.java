package menu;
import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import element.tree.propriedades.Cor;
@SuppressWarnings("serial")
public class Toggle extends JMenuItem{
	private boolean pressed=false;
	private Color corPadrao;
	private Runnable acao;
	public Toggle(JMenuBar menu,String nome,Runnable acao){
		super(nome);
		this.acao=acao;
		setBackground(corPadrao=menu.getBackground());
		setForeground(menu.getForeground());
		final AbstractAction run=new AbstractAction(){
			public void actionPerformed(ActionEvent a){
				setToggle(!pressed);
			}
		};
		getActionMap().put("acao",run);
		addActionListener(run);
	}
	public void setAtalho(int mask,int key,boolean show){
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key,mask),"acao");
		if(show)setAccelerator(KeyStroke.getKeyStroke(key,mask));
	}
	public void setToggle(boolean pressed){
		this.pressed=pressed;
		setBackground(pressed?Cor.getChanged(corPadrao,1.2f):corPadrao);
		if(acao!=null)acao.run();
	}
	public boolean isPressed(){return pressed;}
}