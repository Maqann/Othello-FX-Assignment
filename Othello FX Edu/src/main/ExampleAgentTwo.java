package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;
import com.eudycontreras.othello.utilities.GameTreeUtility;

import java.util.List;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * 
 * @author Eudy Contreras
 */
public class ExampleAgentTwo extends Agent{
	public ExampleAgentTwo() {
		this(PlayerTurn.PLAYER_TWO);
	}
	
	public ExampleAgentTwo(String name) {
		super(name, PlayerTurn.PLAYER_TWO);
	}
	
	public ExampleAgentTwo(PlayerTurn playerTurn) {
		super(playerTurn);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Delete the content of this method and Implement your logic here!
	 */

	private static final int search_depth = 4;


	@Override
	public AgentMove getMove(GameBoardState gameState) {
		setSearchDepth(search_depth);

		List<ObjectiveWrapper> moves = AgentController.getAvailableMoves(gameState, playerTurn);

		if(moves.isEmpty()){
			return null;
		}

		PlayerTurn opponent = GameTreeUtility.getCounterPlayer(playerTurn); //PlayerTurn är en enum i ramverket med två värden: PLAYER_ONE (vit) och PLAYER_TWO (svart).
		ObjectiveWrapper bestMove = moves.get(0);
		double bestValue = Double.NEGATIVE_INFINITY;
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;

		nodesExamined++;

		for(ObjectiveWrapper move : moves){ //Går igenom alla dragen agenten kan göra
			GameBoardState child = AgentController.getNewState(gameState, move); //getNewState skapar en kopia av brädet, gamestate ändras inte
			double value = alphaBeta(child, search_depth - 1, alpha, beta, opponent);

			if(value > bestValue){
				bestValue = value;
				bestMove = move;
			}
			alpha = Math.max(alpha, value);
		}
		return new MoveWrapper(bestMove);
	}

	private double alphaBeta(GameBoardState state, int depth, double alpha, double beta, PlayerTurn turn){
		nodesExamined++;// Visa antalet undersökta noder.

		PlayerTurn opponent = GameTreeUtility.getCounterPlayer(turn);
		List<ObjectiveWrapper> moves = AgentController.getAvailableMoves(state, turn);

		if(depth == 0 || moves.isEmpty()){
			return evalute(state);
		}

		if(turn == playerTurn){
			double value = Double.NEGATIVE_INFINITY;
			for(ObjectiveWrapper move : moves){
				GameBoardState child = AgentController.getNewState(state, move);
				value = Math.max(value, alphaBeta(child, depth - 1, alpha, beta, opponent));
				if(value >= beta){
					prunedCounter++;
					break;
				}
				beta = Math.max(alpha, value);
			}
			return value;
		} else {
			double value = Double.POSITIVE_INFINITY;
			for(ObjectiveWrapper move : moves){
				GameBoardState child = AgentController.getNewState(state, move);
				value = Math.min(value, alphaBeta(child, depth - 1, alpha, beta, opponent));
				if(value <= alpha){
					prunedCounter++;
					break;
				}
				beta = Math.min(beta, value);
			}
			return value;
		}
	}

	private double evalute(GameBoardState state){
		return AgentController.getGameEvaluation(state, playerTurn);
	}


	/**
	 * Default template move which serves as an example of how to implement move
	 * making logic. Note that this method does not use Alpha beta pruning and
	 * the use of this method can disqualify you
	 * 
	 * @param gameState
	 * @return
	 */
	private AgentMove getExampleMove(GameBoardState gameState){
		
		int waitTime = UserSettings.MIN_SEARCH_TIME; // 1.5 seconds
		
		ThreadManager.pause(TimeSpan.millis(waitTime)); // Pauses execution for the wait time to cause delay
		
		return AgentController.getExampleMove(gameState, playerTurn); // returns an example AI move Note: this is not AB Pruning
	}

}
