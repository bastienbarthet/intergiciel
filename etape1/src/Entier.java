import java.io.Serializable;


public class Entier implements Serializable
{
	int data;
	public int read(){
		return data;
	}
	public void add(int i){
		data +=i;
	}
	public void mult(int i){
		data *=i;
	}
}
