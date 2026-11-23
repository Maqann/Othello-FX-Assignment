package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.ObjectiveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.Agent;
import com.eudycontreras.othello.enumerations.PlayerTurn;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;
import com.eudycontreras.othello.utilities.GameTreeUtility;

import java.util.List;

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

		int alpha;
		int beta;

		List<ObjectiveWrapper> availableMoves = AgentController.getAvailableMoves(gameState, playerTurn);
		for (int i = 0; i < availableMoves.size(); i++) {
			System.out.println(availableMoves.get(i));
		}

		if (availableMoves.isEmpty()) {
			//return setPlayerTurn(getCounterPlayer());
		}

		ObjectiveWrapper bestMove = null;
		double bestScore = Double.NEGATIVE_INFINITY;

		for (ObjectiveWrapper move : availableMoves) {
			GameBoardState next = AgentController.getNewState(gameState, move); // brädet efter draget
			double score = evaluate(next);                                       // hur bra är det?
			if (score > bestScore) {                                             // bättre än hittills?
				bestScore = score;
				bestMove = move;
			}
		}
		return getExampleMove(gameState);
	}

	private double evaluate(GameBoardState state) {
		double score = AgentController.getGameEvaluation(state, playerTurn);
		// getGameEvaluation räknar alltid från spelare 1:s (vits) håll
		return (playerTurn == PlayerTurn.PLAYER_ONE) ? score : -score;
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
