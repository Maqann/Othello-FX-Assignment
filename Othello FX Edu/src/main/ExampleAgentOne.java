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
import java.util.Random;

import static com.eudycontreras.othello.utilities.GameTreeUtility.getCounterPlayer;

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
public class ExampleAgentOne extends Agent{


	public ExampleAgentOne() {
		this(PlayerTurn.PLAYER_ONE);
	}
	
	public ExampleAgentOne(String name) {
		super(name, PlayerTurn.PLAYER_ONE);
	}
	
	public ExampleAgentOne(PlayerTurn playerTurn) {
		super(playerTurn);
	}

	/**
	 * Delete the content of this method and Implement your logic here!
	 */
	@Override
	public AgentMove getMove(GameBoardState gameState) {
		return testmove(gameState, playerTurn);
	}


	public double minimax(GameBoardState state, PlayerTurn playerTurn, int depth, double alpha, double beta) { // depth: "Hur mång drag framåt agenten tittar"

		if (depth == 0) {
			return AgentController.getGameEvaluation(state, this.playerTurn);
		}

		if (playerTurn == this.playerTurn) {
			double value = Double.NEGATIVE_INFINITY;
			List<ObjectiveWrapper> availableMoves = AgentController.getAvailableMoves(state, playerTurn);
			for (int i = 0; i < availableMoves.size(); i++) {

				ObjectiveWrapper move = availableMoves.get(i);
				GameBoardState child = AgentController.getNewState(state, move);
				PlayerTurn nextPlayer = GameTreeUtility.getCounterPlayer(playerTurn);

				double score = minimax(child, nextPlayer, depth - 1, alpha, beta);
				value = Math.max(alpha, score); //if sats, samma score > bestScore
				alpha = Math.max(value, score);

				if (value >= beta) {
					break;
				}
			}
			return value;

		} else {

			double value = Double.POSITIVE_INFINITY;
			List<ObjectiveWrapper> availableMoves = AgentController.getAvailableMoves(state, playerTurn);
			for (int i = 0; i < availableMoves.size(); i++) {

				ObjectiveWrapper move = availableMoves.get(i);
				GameBoardState child = AgentController.getNewState(state, move);
				PlayerTurn nextPlayer = GameTreeUtility.getCounterPlayer(playerTurn);

				double score = minimax(child, nextPlayer, depth - 1, alpha, beta);
				value = Math.min(beta, score);
				beta = Math.min(value, score);

				if (value <= alpha) {
					break;
				}
			}
			return value;
		}
	}

	/*
	public static MoveWrapper testmove(GameBoardState gameState, PlayerTurn playerTurn) {

			@Override
					isValid
		return new MoveWrapper(minimaxfuck);


	}
	*/

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
