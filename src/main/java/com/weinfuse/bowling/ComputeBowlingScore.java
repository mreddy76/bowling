package com.weinfuse.bowling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class computes the frame scores based on the below understanding.
 * https://www.myactivesg.com/Sports/Bowling/How-To-Play/Bowling-Rules/How-are-points-determined-in-bowling
 * @author Manohar
 *
 */
public class ComputeBowlingScore {
	
	public static void main(String[] args) {
		ComputeBowlingScore computeBowlingScore = new ComputeBowlingScore();
		
		String[] rollsArray ={"X", "9", "/", "5", "/", "7", "2", "X", "X", "X", "9", "0", "8", "/", "9", "/", "X"};
		List<String> rolls = Arrays.asList(rollsArray);
		List<Frame> frames = computeBowlingScore.computeScore(rolls);
		for (int i = 0; i < frames.size(); i++) {
			System.out.println("Frame: "+(i+1)+ ", Score: " + frames.get(i).getScore()+"\n");
		}	
	}
	
	/**
	 * Takes a list of rolls and computes frame scores
	 * @param rollScores
	 * @return
	 */
	public List<Integer> computeScoreAsPerRequested( List<String> rollScores) {
		List<Integer> calculatedScores = new ArrayList<Integer>();
		List<Frame> frames = computeScore(rollScores);
		for (Frame frame : frames) {
			if (frame.isComplete())
				calculatedScores.add(frame.getScore());
		}
		return calculatedScores;
	}
	
	/**
	 * Takes a list of rolls and computes frame scores
	 * @param rollScores
	 * @return
	 */
	public List<Frame> computeScore( List<String> rollScores) {
		List<Frame> frames = new ArrayList<Frame>();
		for (String rollScore : rollScores) {
			if (rollScore.equals("X")) {
				if (frames.size() == 10) {
					Frame frame = frames.get(frames.size()-1);
					// could be second roll or third roll
					int scoreForThisFrameSoFar = 10 + frame.getScore();					
					frame.setScore(scoreForThisFrameSoFar);
					
					if (frame.getFirstRollScore() < 10 || frame.getScore() > 20) {
						// then third roll
						frame.setComplete(true);
						adjustScoreForPriorTwoFrames(frames);
					}
				} else {
					Frame frame = new Frame();
					frame.setFirstRollScore(10);
					frame.setScore(10);
					frame.setHasStrike(true);
					// add it to list
					frames.add(frame);
					
					if (frames.size() < 10) {
						// don't mark it complete yet
						frame.setComplete(true);
						adjustScoreForPriorTwoFrames(frames);
					}					
				}
				
			} else if (rollScore.equals("/")) {
				Frame frame = frames.get(frames.size()-1);
				
				if (frames.size() == 10) {
					// if third roll
					if (frame.getFirstRollScore() == 10) {
						frame.setComplete(true);
						frame.setScore(20);
						adjustScoreForPriorTwoFrames(frames);
					} else {
						// if second roll, then the frame is still not complete
						frame.setScore(10);
						frame.setHasSpare(true);
					}
				} else {
					frame.setScore(10);
					frame.setHasSpare(true);
					frame.setComplete(true);
					adjustScoreForPriorTwoFrames(frames);
				}					
				
			} else {
				if (frames.size() < 10) {
					Frame frame = frames.size() > 0 ? frames.get(frames.size()-1) : null;
					
					if (frame!=null && !frame.isComplete()) {
						// it is a second pass
						int scoreForThisFrameSoFar = Integer.valueOf(rollScore) + frame.getScore();					
						frame.setScore(scoreForThisFrameSoFar);
						frame.setComplete(true);
						adjustScoreForPriorTwoFrames(frames);
					
					} else if (frame==null || (frame!=null && frame.isComplete())) {
						// first pass
						frame = new Frame();
						int currentRollScore = Integer.valueOf(rollScore);
						frame.setScore(currentRollScore);
						frame.setFirstRollScore(currentRollScore);
						// add it to list
						frames.add(frame);
					}
				}
			}
		}
		return frames;
	}
	
	/**
	 * Adjust scores based on the below rules
	 * *********************************************************************
	 * Spare: 10 points + the number of pins you knock down for your first attempt at the next frame.
	 * 
	 * Strike: score 10 points + the number of pins you knock down for the entire next frame.
	 * 
	 * Double:
	 * 1st frame – 20 points + the number of pins you knock down in 3rd frame 
	 * 2nd frame – same as scoring for strike
	
	 * Turkey:
	 * 1st frame - 30 points 
	 * 2nd frame – same as scoring for double
	 * 3rd frame – same as scoring for strike
	
	 * Four-Bagger:
	 * 1st frame - 30 points 
	 * 2nd frame - 30 points 
	
	 * 3rd frame – same as scoring for double
	 * 4th frame – same as scoring for strike
	 * **********************************************************************
	 * @param frames
	 */
	private void adjustScoreForPriorTwoFrames(List<Frame> frames) {

		int currentIndex = frames.size()-1;
		Frame currentFrame = frames.get(currentIndex);
		Frame priorFrame = null;
		Frame priorToPriorFrame = null;

		if ((currentIndex - 1) >= 0)
			priorFrame = frames.get(currentIndex - 1);
		if ((currentIndex - 2) >= 0)
			priorToPriorFrame = frames.get(currentIndex - 2);

		// check if the frame prior to the current one has a strike
		if (priorFrame != null && priorFrame.hasStrike) {
			int adjustedPriorFrameScore = priorFrame.getScore() + currentFrame.getScore();
			priorFrame.setScore(adjustedPriorFrameScore > 30 ? 30 : adjustedPriorFrameScore);
			// check if the frame prior to this has a strike
			if (priorToPriorFrame != null && priorToPriorFrame.hasStrike) {
				int adjustedPriorToPriorFrameScore = priorToPriorFrame.getScore() + currentFrame.getScore();
				priorToPriorFrame.setScore(adjustedPriorToPriorFrameScore > 30 ? 30 : adjustedPriorToPriorFrameScore);
			}

		}
		// check if the frame prior to the current one has a spare
		else if (priorFrame != null && priorFrame.hasSpare) {
			int adjustedPriorFrameScore = priorFrame.getScore() + currentFrame.getFirstRollScore();
			priorFrame.setScore(adjustedPriorFrameScore > 30 ? 30 : adjustedPriorFrameScore);
			if (priorToPriorFrame != null) {

			}
		}
	}
	
	class Frame {
		
		private boolean hasStrike;
		private boolean hasSpare;
		private boolean complete;
		private int firstRollScore;
		private int score; // max restricted to 30
		
		public boolean isHasStrike() {
			return hasStrike;
		}
		public void setHasStrike(boolean hasStrike) {
			this.hasStrike = hasStrike;
		}
		public boolean isHasSpare() {
			return hasSpare;
		}
		public void setHasSpare(boolean hasSpare) {
			this.hasSpare = hasSpare;
		}
		public boolean isComplete() {
			return complete;
		}
		public void setComplete(boolean complete) {
			this.complete = complete;
		}
		public int getFirstRollScore() {
			return firstRollScore;
		}
		public void setFirstRollScore(int firstRollScore) {
			this.firstRollScore = firstRollScore;
		}
		public int getScore() {
			return score;
		}
		public void setScore(int score) {
			this.score = score;
		}
		@Override
		public String toString() {
			return "Frame [hasStrike=" + hasStrike + ", hasSpare=" + hasSpare + ", complete=" + complete
					+ ", firstRollScore=" + firstRollScore + ", score=" + score + "]";
		}
			
		
	}
}