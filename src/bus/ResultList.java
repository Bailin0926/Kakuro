package bus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

public class ResultList implements Serializable{
 
	private static final long serialVersionUID = 4086944852573858947L;
	private ArrayList<Result> resultList;

	public ArrayList<Result> getResultList() {
		return resultList;
	}

	public void setResultList(ArrayList<Result> resultList) {
		this.resultList = resultList;
	}

	public ResultList() {
		this.resultList = new ArrayList<Result>();
	}
	public void add(Result r) {
		this.resultList.add(r);
		this.sortList();
	}
	
	public void sortList() {
		Collections.sort(resultList);
	}
}
