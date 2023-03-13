package bus;

import java.io.Serializable;

public class Result implements Serializable, Comparable<Result>{

	private static final long serialVersionUID = 6013523893278089745L;
	private long finishTime;
	private String player;
	private Double score;
	public long getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(long finishTime) {
		this.finishTime = finishTime;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Result(long finishTime, String player, Double score) {
		super();
		this.finishTime = finishTime;
		this.player = player;
		this.score = score;
	}
	@Override
	public int compareTo(Result o) {
		return ((this.getScore() < o.getScore()) ? (1) : ((this.getScore() == o.getScore()) ? 0 : -1));
	}
	
}
