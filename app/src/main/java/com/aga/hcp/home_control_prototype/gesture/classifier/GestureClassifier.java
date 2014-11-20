/*
 * GestureClassifier.java
 *
 * Created: 18.08.2011
 *
 * Copyright (C) 2011 Robert Nesselrath
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.aga.hcp.home_control_prototype.gesture.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import android.content.Context;


import com.aga.hcp.home_control_prototype.Global;
import com.aga.hcp.home_control_prototype.gesture.Gesture;
import com.aga.hcp.home_control_prototype.gesture.classifier.featureExtraction.IFeatureExtractor;


public class GestureClassifier {

	protected List<Gesture> trainingSet = Collections.emptyList();
	protected IFeatureExtractor featureExtractor;
	protected String activeTrainingSet = "";
	private final Context context;
    private Gesture g,g1;

	public GestureClassifier(IFeatureExtractor fE, Context context) {
		trainingSet = new ArrayList<Gesture>();
		featureExtractor = fE;
		this.context = context;
        addDefaultData();
	}

	public boolean commitData() {
		if (activeTrainingSet != null && activeTrainingSet != "") {
			try {
				FileOutputStream fos = new FileOutputStream(new File(context.getExternalFilesDir(null), activeTrainingSet + ".gst").toString());
                ObjectOutputStream o = new ObjectOutputStream(fos);
				o.writeObject(trainingSet);
				o.close();
				fos.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean trainData(String trainingSetName, Gesture signal) {
		loadTrainingSet(trainingSetName);
		trainingSet.add(featureExtractor.sampleSignal(signal));
		return true;
	}

	@SuppressWarnings("unchecked")
	public void loadTrainingSet(String trainingSetName) {
		if (!trainingSetName.equals(activeTrainingSet)) {
			activeTrainingSet = trainingSetName;
			FileInputStream input;
			ObjectInputStream o;
			try {
				input = new FileInputStream(new File(context.getExternalFilesDir(null), activeTrainingSet + ".gst"));
				o = new ObjectInputStream(input);
				trainingSet = (ArrayList<Gesture>) o.readObject();
				try {
					o.close();
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				trainingSet = new ArrayList<Gesture>();
			}
		}
        trainingSet.add(g1);
        trainingSet.add(g);
	}
    public void addDefaultData()
    {
        float[] temp1_1={0.73197764f,0.0f,0.36748266f}; float[] temp1_2={0.7924054f,0.05859677f,0.35374904f}; float[] temp1_3={0.8518214f,0.10032768f,0.3497976f};
        float[] temp1_4={0.9095027f,0.11314571f,0.36261564f}; float[] temp1_5={0.91186386f,0.1610447f,0.33967808f}; float[] temp1_6={0.8959136f,0.212606f,0.30628374f};
        float[] temp1_7={0.8922513f,0.18880107f,0.29255012f}; float[] temp1_8={0.88121617f,0.14851578f,0.29139358f}; float[] temp1_9={0.86198914f,0.08991903f,0.30421165f};
        float[] temp1_10={0.84661716f,0.07064379f,0.31548765f}; float[] temp1_11={0.8421838f,0.05835582f,0.32628176f}; float[] temp1_12={0.87606f,0.044622213f,0.3363531f};
        float[] temp1_13={0.9162972f,0.034599077f,0.35437548f}; float[] temp1_14={0.9611603f,0.027274478f,0.37818038f}; float[] temp1_15={0.9826041f,0.01214341f,0.4028527f};
        float[] temp1_16={0.9972533f,0.0014938475f,0.4194776f}; float[] temp1_17={1.0f,0.0014938475f,0.41856205f}; float[] temp1_18={0.9964822f,0.013396306f,0.42015222f};
        float[] temp1_19={0.9891094f,0.031081341f,0.42256168f}; float[] temp1_20={0.9643889f,0.05397072f,0.41889933f}; float[] temp1_21={0.950077f,0.07454703f,0.4175501f};
        float[] temp1_22={0.950077f,0.09194295f,0.41938123f}; float[] temp1_23={0.93200654f,0.0941596f,0.4421742f}; float[] temp1_24={0.90550303f,0.097099066f,0.46766573f};
        float[] temp1_25={0.8597243f,0.12548189f,0.47773707f};float[] temp1_26={0.82840204f,0.16494793f,0.4719063f}; float[] temp1_27={0.81009054f,0.21438898f,0.45176366f};
        float[] temp1_28={0.7975134f,0.25318044f,0.4308018f};float[] temp1_29={0.78874314f,0.26445633f,0.4225135f};float[] temp1_30={0.7887432f,0.20219745f,0.44998065f};
        float[] temp1_31={0.5967619f,0.22320735f,0.39533547f}; float[] temp1_32={0.7125578f,0.21511178f,0.4541249f}; List<float[]> gest1_hard=new ArrayList<float[]>();
        gest1_hard.add(temp1_1); gest1_hard.add(temp1_2); gest1_hard.add(temp1_3);
        gest1_hard.add(temp1_4); gest1_hard.add(temp1_5); gest1_hard.add(temp1_6);
        gest1_hard.add(temp1_7); gest1_hard.add(temp1_8); gest1_hard.add(temp1_9);
        gest1_hard.add(temp1_10); gest1_hard.add(temp1_11); gest1_hard.add(temp1_12);
        gest1_hard.add(temp1_13); gest1_hard.add(temp1_14); gest1_hard.add(temp1_15);
        gest1_hard.add(temp1_16); gest1_hard.add(temp1_17); gest1_hard.add(temp1_18);
        gest1_hard.add(temp1_19); gest1_hard.add(temp1_20);gest1_hard.add(temp1_21);gest1_hard.add(temp1_22);
        gest1_hard.add(temp1_23);gest1_hard.add(temp1_24);gest1_hard.add(temp1_25);
        gest1_hard.add(temp1_26);gest1_hard.add(temp1_27);gest1_hard.add(temp1_28);
        gest1_hard.add(temp1_29);gest1_hard.add(temp1_30);gest1_hard.add(temp1_31);
        gest1_hard.add(temp1_32);
        g=new Gesture(gest1_hard,Global.DEFAULT_GESTURE_LIST[1]);

        float[] temp2_1={0.16953915f,0.13082302f,0.8412639f}; float[] temp2_2={0.17328587f,0.115836136f,0.8618709f}; float[] temp2_3={0.17703259f,0.100849256f,0.88247776f};
        float[] temp2_4={0.18515049f,0.07712001f,0.89783937f}; float[] temp2_5={0.19358061f,0.052766323f,0.9128262f}; float[] temp2_6={0.18739852f,0.036530536f,0.9018358f};
        float[] temp2_7={0.1789684f,0.021543665f,0.886849f}; float[] temp2_8={0.15705007f,0.010303495f,0.8778569f}; float[] temp2_9={0.13175972f,0.0f,0.87036335f};
        float[] temp2_10={0.15317848f,0.008242787f,0.8525665f}; float[] temp2_11={0.19158238f,0.023229681f,0.83102286f}; float[] temp2_12={0.23248407f,0.040714357f,0.824466f};
        float[] temp2_13={0.27463472f,0.059447978f,0.82540274f}; float[] temp2_14={0.29093292f,0.05682526f,0.8375796f}; float[] temp2_15={0.28999624f,0.03996502f,0.85724986f};
        float[] temp2_16={0.26458097f,0.024603454f,0.89340585f}; float[] temp2_17={0.21774699f,0.010553259f,0.94398654f}; float[] temp2_18={0.18490072f,0.006993871f,0.967466f};
        float[] temp2_19={0.16804044f,0.015424017f,0.9599725f}; float[] temp2_20={0.16991381f,0.021980764f,0.9427376f}; float[] temp2_21={0.19988762f,0.025727488f,0.9108904f};
        float[] temp2_22={0.22861244f,0.03134756f,0.89496684f}; float[] temp2_23={0.25483954f,0.040714383f,0.9108904f}; float[] temp2_24={0.2793181f,0.05008118f,0.92006993f};
        float[] temp2_25={0.2989884f,0.059448f,0.91070306f};float[] temp2_26={0.31734732f,0.06769076f,0.9125765f}; float[] temp2_27={0.33046085f,0.07143751f,0.9594104f};
        float[] temp2_28={0.34482327f,0.07306107f,1.0f};float[] temp2_29={0.3673036f,0.060884226f,1.0f};float[] temp2_30={0.38453853f,0.056700382f,0.97652054f};
        float[] temp2_31={0.32833767f,0.16441873f,0.6243284f}; float[] temp2_32={0.33601847f,0.04371174f,0.95741224f};
        List<float[]> gest2_hard=new ArrayList<float[]>();
        gest2_hard.add(temp2_1); gest2_hard.add(temp2_2); gest2_hard.add(temp2_3);
        gest2_hard.add(temp2_4); gest2_hard.add(temp2_5); gest2_hard.add(temp2_6);
        gest2_hard.add(temp2_7); gest2_hard.add(temp2_8); gest2_hard.add(temp2_9);
        gest2_hard.add(temp2_10); gest2_hard.add(temp2_11); gest2_hard.add(temp2_12);
        gest2_hard.add(temp2_13); gest2_hard.add(temp2_14); gest2_hard.add(temp2_15);
        gest2_hard.add(temp2_16); gest2_hard.add(temp2_17); gest2_hard.add(temp2_18);
        gest2_hard.add(temp2_19); gest2_hard.add(temp2_20);gest2_hard.add(temp2_21);gest2_hard.add(temp2_22);
        gest2_hard.add(temp2_23);gest2_hard.add(temp2_24);gest2_hard.add(temp2_25);
        gest2_hard.add(temp2_26);gest2_hard.add(temp2_27);gest2_hard.add(temp2_28);
        gest2_hard.add(temp2_29);gest2_hard.add(temp2_30);gest2_hard.add(temp2_31);
        gest2_hard.add(temp2_32);
        g1=new Gesture(gest2_hard, Global.DEFAULT_GESTURE_LIST[0]);
    }
	public boolean checkForLabel(String trainingSetName, String label) {
		loadTrainingSet(trainingSetName);
		for (Gesture s : trainingSet) {
			if (s.getLabel().equals(label)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkForTrainingSet(String trainingSetName) {
		File file = new File(trainingSetName + ".gst");
		return file.exists();
	}

	public boolean deleteTrainingSet(String trainingSetName) {
		System.out.printf("Try to delete training set %s\n", trainingSetName);
		if (activeTrainingSet != null && activeTrainingSet.equals(trainingSetName)) {
			trainingSet = new ArrayList<Gesture>();
		}
		File file = new File(context.getExternalFilesDir(null),  activeTrainingSet + ".gst");
		if (file.exists()) {
		    file.delete();
		    return true;
		}
		//return false;
		return context.deleteFile(activeTrainingSet + ".gst");

	}

	public boolean deleteLabel(String trainingSetName, String label) {
		loadTrainingSet(trainingSetName);
		boolean labelExisted = false;
		ListIterator<Gesture> it = trainingSet.listIterator();
		while (it.hasNext()) {
			Gesture s = it.next();
			if (s.getLabel().equals(label)) {
				it.remove();
				labelExisted = true;
			}
		}
		return labelExisted;
	}

	public List<String> getLabels(String trainingSetName) {
		loadTrainingSet(trainingSetName);
		List<String> labels = new ArrayList<String>();

		for (Gesture s : trainingSet) {
			if (!labels.contains(s.getLabel())) {
				labels.add(s.getLabel());
			}
		}
		return labels;
	}

	public IFeatureExtractor getFeatureExtractor() {
		return featureExtractor;
	}

	public void setFeatureExtractor(IFeatureExtractor featureExtractor) {
		this.featureExtractor = featureExtractor;
	}

	public Distribution classifySignal(String trainingSetName, Gesture signal) {
		if (trainingSetName == null) {
			System.err.println("No Training Set Name specified");
			trainingSetName = "default";
		}
		if (!trainingSetName.equals(activeTrainingSet)) {
			loadTrainingSet(trainingSetName);
		}

		Distribution distribution = new Distribution();
		Gesture sampledSignal = featureExtractor.sampleSignal(signal);

		for (Gesture s : trainingSet) {
			double dist = DTWAlgorithm.calcDistance(s, sampledSignal);
			distribution.addEntry(s.getLabel(), dist);
		}
		if (trainingSet.isEmpty()) {
			System.err.printf("No training data for trainingSet %s available.\n", trainingSetName);
		}

		return distribution;
	}

}