package menu;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
@SuppressWarnings("serial")
public class Botao extends JMenuItem{
	public Botao(JMenuBar menu,String nome,AbstractAction acao){
		super(nome);
		setBackground(menu.getBackground());
		setForeground(menu.getForeground());
		getActionMap().put("acao",acao);
		addActionListener(acao);
	}
	public void setAtalho(int mask,int key,boolean show){
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(key,mask),"acao");
		if(show)setAccelerator(KeyStroke.getKeyStroke(key,mask));
	}
}