package com.example.testingar;
import static androidx.core.view.accessibility.AccessibilityRecordCompat.setSource;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.concurrent.CompletableFuture;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable modelRenderable; // To hold our loaded 3D model

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        // --- Load Your 3D Model ---

        CompletableFuture<ModelRenderable> modelFuture = ModelRenderable.builder()
                .setSource(this, Uri.parse("andy.glb"))
                .build();

        modelFuture.thenAccept(renderable -> modelRenderable = renderable)

                .exceptionally(throwable -> {
                    Toast.makeText(this, "Unable to load model: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                    return null; // Return null to indicate failure
                });

        // This listener is triggered when the user taps on a detected AR plane.
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            // Check if the 3D model has finished loading
            if (modelRenderable == null) {
                Toast.makeText(this, "Model is still loading. Please wait.", Toast.LENGTH_SHORT).show();
                return; // Exit if the model isn't ready
            }

            // Ensure the detected plane is a horizontal upward-facing plane (like a floor or table)
            if (plane.getType() != Plane.Type.HORIZONTAL_UPWARD_FACING) {
                Toast.makeText(this, "Please tap on a flat, horizontal surface (e.g., floor, table).", Toast.LENGTH_SHORT).show();
                return; // Exit if not a suitable plane
            }

            // Create an ARCore Anchor at the point where the user tapped.

            Anchor anchor = hitResult.createAnchor();


            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(arFragment.getArSceneView().getScene()); // Attach to the AR scene


            TransformableNode modelNode = new TransformableNode(arFragment.getTransformationSystem());
            modelNode.setParent(anchorNode); // Attach to the AnchorNode
            modelNode.setRenderable(modelRenderable);
            modelNode.select();

            Toast.makeText(this, "3D object placed!", Toast.LENGTH_SHORT).show();
        });
    }
}