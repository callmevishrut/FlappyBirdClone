package com.ignite.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;

	//fonts fo scoring
	BitmapFont font;

	//Scoring

	int score = 0;
	int scoringTube = 0;
	//Collision checks for bird
	Circle birdCircle;
	//ShapeRenderer shapeRenderer;

	//Collison checks for pipes
	Rectangle[] topTubeRecs;
	Rectangle[] bottomTubeRecs;


	int flapState = 0;

	float birdY = 0;
	float velocity = 0;

	int gameState = 0;
	float gravity = 2;

	Texture topTube;
	Texture bottomTube;

	float maxTubeOffset;

	Random randomGenerator;


	float gap = 400;

	float tubeVelocity = 4;


	float numberOfTubes = 4;

	float[] tubeX = new float[4];
	float[] tubeOffset = new float[4];
	float distanceBetweemTubes;





	@Override
	public void create () { // when the app is run

		//setting up font
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().scale(10f);


		// for collision
		birdCircle = new Circle();
		//shapeRenderer = new ShapeRenderer();


		batch = new SpriteBatch(); // a batch for a no. of sprites
		background = new Texture("bg.png"); //for background
		birds = new Texture[2];

		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight()/2 - birds[0].getHeight()/2;

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");

		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 -100;
		randomGenerator = new Random();
		distanceBetweemTubes = Gdx.graphics.getWidth()  / 2 ;

		topTubeRecs = new Rectangle[(int) numberOfTubes];
		bottomTubeRecs = new Rectangle[(int) numberOfTubes];

		for ( int i = 0 ; i < numberOfTubes; i++) {
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i *distanceBetweemTubes;

			topTubeRecs[i] = new Rectangle();
			bottomTubeRecs[i] = new Rectangle();
		}


	}

	@Override
	public void render () { // when the app is processed
		// the order in which the texure are drawed  are set as their layers

		batch.begin(); // to get the textures
		batch.draw(background, 0 , 0 , Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if (gameState != 0) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2){
				score++;
				if(scoringTube < numberOfTubes - 1) {
					scoringTube++;

				}else
					scoringTube = 0;
			}


			if (Gdx.input.justTouched()) {
				velocity = -30;

			}

			for(int i = 0 ;i< numberOfTubes;i++) {

				if (tubeX[i] < - topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweemTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;



				}
				batch.draw(topTube,tubeX[i], Gdx.graphics.getHeight() / 2 + gap /2 +tubeOffset[i]);
				batch.draw(bottomTube,tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRecs[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 + gap /2 +tubeOffset[i],topTube.getWidth(),topTube.getHeight());
				bottomTubeRecs[i] = new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			}

			if (birdY > 0 || velocity < 0) {
				velocity += gravity;
				birdY -= velocity;
			}
		} else {
				if (Gdx.input.justTouched()) {
					//Gdx.app.log("Tounched","Yep"); // for logging in a GDX game
					gameState = 1;
				}
		}
		if ( flapState == 0) {
			flapState = 1;
		}
		else {
			flapState = 0 ;
		}



		batch.draw(birds[flapState],Gdx.graphics.getWidth()/2 - birds[flapState].getWidth() / 2,birdY);
		font.draw(batch,String.valueOf(score),100,200);

		birdCircle.set(Gdx.graphics.getWidth() / 2 , birdY + birds[flapState].getHeight() / 2 , birds[flapState].getWidth() / 2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);

		//shapeRenderer.circle(birdCircle.x,birdCircle.y , birdCircle.radius);
		batch.end();

		for (int i = 0 ; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap /2 +tubeOffset[i],topTube.getWidth(),topTube.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

			if (Intersector.overlaps(birdCircle,topTubeRecs[i]) || Intersector.overlaps(birdCircle,bottomTubeRecs[i])){
				//Collision code here
			}
		}
		//shapeRenderer.end();
	}
	


}
