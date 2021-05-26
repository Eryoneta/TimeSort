package tree;
import java.time.LocalDateTime;
import element.tree.objeto.modulo.Modulo;
public class ModuloTime extends Modulo{
	private LocalDateTime dataFinal;
		public LocalDateTime getDataFinal(){return dataFinal;}
	private LocalDateTime dataAbsoluta;
		public void setAno(LocalDateTime data){dataAbsoluta=data;}
		public LocalDateTime getDataAbsoluta(){return dataAbsoluta;}
}