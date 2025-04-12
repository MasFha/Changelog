package com.changelog.mdevz;

import com.changelog.mdevz.model.Changelog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;

public class GithubApiClient {
	private static final String TAG = "GithubApiClient";
	private final OkHttpClient client;
	private final String token;
	private final String owner;
	private final String repo;
	private final boolean isPrivate;
	private final String jsonPath;
	private final String mainBranch;
	private final Gson gson;
	
	public GithubApiClient(
	String token,
	String owner,
	String repo,
	boolean isPrivate,
	String jsonPath,
	String mainBranch) {
		this.token = token;
		this.owner = owner;
		this.repo = repo;
		this.isPrivate = isPrivate;
		this.jsonPath = jsonPath;
		this.mainBranch = mainBranch;
		this.client = new OkHttpClient();
		this.gson = new Gson();
	}
	
	public interface Callback<T> {
		void onSuccess(T result);
		void onError(String error);
	}
	
	public void uploadChangelog(Changelog changelog, Callback<Void> callback) {
		new Thread(() -> {
			try {
				// Validate input
				if (changelog == null
				|| changelog.getVersion() == null
				|| changelog.getDate() == null
				|| changelog.getMessage() == null
				|| changelog.getDeveloper() == null) {
					callback.onError("Invalid changelog data");
					return;
				}
				
				// Get existing file content (if any)
				List<Changelog> existingChangelogs = fetchExistingChangelogs();
				if (existingChangelogs == null) {
					existingChangelogs = new ArrayList<>();
				}
				
				// Add new changelog to the list
				existingChangelogs.add(0, changelog); // Add to the beginning for newest first
				
				// Convert to JSON array
				String jsonContent = gson.toJson(existingChangelogs);
				
				// Get existing file SHA (if any)
				String sha = getFileSha();
				
				// Prepare request body
				JSONObject content = new JSONObject();
				content.put("message", "Update changelog v" + changelog.getVersion());
				content.put(
				"content",
				Base64.getEncoder().encodeToString(jsonContent.getBytes()));
				content.put("branch", mainBranch);
				if (sha != null) {
					content.put("sha", sha);
				}
				
				// Send request to GitHub
				RequestBody body =
				RequestBody.create(
				MediaType.get("application/json; charset=utf-8"), content.toString());
				Request request =
				new Request.Builder()
				.url("https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + jsonPath)
				.put(body)
				.addHeader("Authorization", "Bearer " + token)
				.addHeader("Accept", "application/vnd.github.v3+json")
				.build();
				
				try (Response response = client.newCall(request).execute()) {
					if (response.isSuccessful()) {
						callback.onSuccess(null);
						} else {
						String errorBody = response.body() != null ? response.body().string() : "No error details";
						callback.onError("Upload failed: HTTP " + response.code() + " - " + errorBody);
					}
				}
				} catch (Exception e) {
				callback.onError("Unexpected error: " + e.getMessage());
			}
		}).start();
	}
	
	public void fetchChangelogs(Callback<List<Changelog>> callback) {
		new Thread(() -> {
			try {
				String url =
				String.format(
				"https://raw.githubusercontent.com/%s/%s/%s/%s",
				owner, repo, mainBranch, jsonPath);
				Request request =
				new Request.Builder()
				.url(url)
				.addHeader("Authorization", "Bearer " + token)
				.addHeader("Accept", "application/vnd.github.v3+json")
				.build();
				
				try (Response response = client.newCall(request).execute()) {
					if (!response.isSuccessful()) {
						callback.onError("Failed to fetch changelog: HTTP " + response.code());
						return;
					}
					
					String jsonData = response.body().string();
					Type listType = new TypeToken<ArrayList<Changelog>>() {}.getType();
					List<Changelog> changelogs = gson.fromJson(jsonData, listType);
					if (changelogs == null) {
						changelogs = new ArrayList<>();
					}
					callback.onSuccess(changelogs);
				}
				} catch (Exception e) {
				callback.onError("Error fetching changelog: " + e.getMessage());
			}
		}).start();
	}
	
	private List<Changelog> fetchExistingChangelogs() {
		try {
			String url =
			String.format(
			"https://raw.githubusercontent.com/%s/%s/%s/%s",
			owner, repo, mainBranch, jsonPath);
			Request request =
			new Request.Builder()
			.url(url)
			.addHeader("Authorization", "Bearer " + token)
			.addHeader("Accept", "application/vnd.github.v3+json")
			.build();
			
			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful() && response.body() != null) {
					String jsonData = response.body().string();
					Type listType = new TypeToken<ArrayList<Changelog>>() {}.getType();
					return gson.fromJson(jsonData, listType);
				}
			}
			} catch (Exception e) {
			// Log error if needed
		}
		return null;
	}
	
	private String getFileSha() {
		try {
			String url =
			String.format(
			"https://api.github.com/repos/%s/%s/contents/%s?ref=%s",
			owner, repo, jsonPath, mainBranch);
			Request request =
			new Request.Builder()
			.url(url)
			.get()
			.addHeader("Authorization", "Bearer " + token)
			.addHeader("Accept", "application/vnd.github.v3+json")
			.build();
			
			try (Response response = client.newCall(request).execute()) {
				if (response.isSuccessful() && response.body() != null) {
					String bodyString = response.body().string();
					JSONObject json = new JSONObject(bodyString);
					return json.getString("sha");
				}
			}
			} catch (Exception e) {
			// Log error if needed
		}
		return null;
	}
}