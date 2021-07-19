package com.weinfuse.bowling;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.weinfuse.bowling.ComputeBowlingScore.Frame;

import junit.framework.Assert;

public class ComputeBowlingScoreTest {

	@Test
	public void testComputeScore() {
		ComputeBowlingScore computeBowlingScore = new ComputeBowlingScore();

		String[] rollsArray = { "X", "9", "/", "5", "/", "7", "2", "X", "X", "X", "9", "0", "8", "/", "9", "/", "X" };
		List<String> rolls = Arrays.asList(rollsArray);
		List<Frame> frames = computeBowlingScore.computeScore(rolls);
		for (int i = 0; i < frames.size(); i++) {
			System.out.println("Frame: " + (i + 1) + ", Score: " + frames.get(i).getScore() + "\n");
		}
	}
	
	@Test
	public void testcomputeScoreAsPerRequested_variation_1() {
		ComputeBowlingScore computeBowlingScore = new ComputeBowlingScore();

		String[] rollsArray = { "X", "9" };
		List<String> rolls = Arrays.asList(rollsArray);
		List<Integer> calculatedScores = computeBowlingScore.computeScoreAsPerRequested(rolls);
		
		Assert.assertEquals("There should only be one frame score as the second frame is not complete", calculatedScores.size(), 1);
		Assert.assertEquals("First Frame Score should be 10 as it is a strike", calculatedScores.get(0).intValue(), 10);
	}  
	
	@Test
	public void testcomputeScoreAsPerRequested_variation_2() {
		ComputeBowlingScore computeBowlingScore = new ComputeBowlingScore();

		String[] rollsArray = { "X", "9", "/"};
		List<String> rolls = Arrays.asList(rollsArray);
		List<Integer> calculatedScores = computeBowlingScore.computeScoreAsPerRequested(rolls);
		
		Assert.assertEquals("There should be two frames that are complete", calculatedScores.size(), 2);
		Assert.assertEquals("First Frame Score should be 20 as it is a strike", calculatedScores.get(0).intValue(), 20);
		Assert.assertEquals("Second Frame Score should be 10 as it is a spare", calculatedScores.get(1).intValue(), 10);
	}  

}
