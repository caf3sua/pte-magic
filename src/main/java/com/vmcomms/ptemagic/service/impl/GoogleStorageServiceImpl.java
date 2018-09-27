package com.vmcomms.ptemagic.service.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.BucketInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.vmcomms.ptemagic.service.GoogleStorageService;
import com.vmcomms.ptemagic.service.util.DateUtil;


/**
 * Service Implementation for Google storage.
 */
@Service
@Transactional
public class GoogleStorageServiceImpl implements GoogleStorageService {

    private final Logger log = LoggerFactory.getLogger(GoogleStorageServiceImpl.class);

    @Autowired
    private Environment env;
    
    public String upload(MultipartFile file, String type) throws IOException {
		Long currentTime = System.currentTimeMillis();
		Storage storage;
		Resource resource = new ClassPathResource("config/" + env.getProperty("google-cloud.storage.credential-file"));

		storage = StorageOptions.newBuilder().setProjectId(env.getProperty("google-cloud.storage.project-id"))
				.setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream())).build().getService();

		// Create a bucket
		String bucketName = getBucketName(storage, type);
		
		String filename = "";
		if (StringUtils.equals("question", type)) {
			filename = currentTime + "_" + file.getOriginalFilename();
		} else {
			filename = file.getOriginalFilename();
		}
				
		BlobId blobId = BlobId.of(bucketName, filename);

		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

		byte[] content = file.getBytes();
		storage.create(blobInfo, content);
		storage.createAcl(blobId, Acl.of(User.ofAllUsers(), Role.READER));

		// Process resize to create thumb
		String audioUrl = "https://storage.googleapis.com/" + bucketName + "/" + filename;
		
		return audioUrl;
	}
    
    private String getBucketName(Storage storage, String type) {
    	// Get a bucket name
		String bucketName = env.getProperty("google-cloud.storage.bucket-name-question");
		if (StringUtils.equals("answer", type)) {
			String prefixBucketName = env.getProperty("google-cloud.storage.bucket-name-answer-prefix"); 
			bucketName = prefixBucketName + "-" + DateUtil.currentMonth();
			createBucket(storage, bucketName);
		}
		
		return bucketName;
    }
    
    private boolean createBucket(Storage storage, String bucketName) {
    	Bucket bucket = storage.get(bucketName);
    	if (bucket == null) {
    		storage.create(BucketInfo.newBuilder(bucketName)
    			    .build());
//    		storage.create(BucketInfo.of(bucketName));
    		return true;
    	}
    	
    	return false;
    }

	@Override
	public void removeUnusedMedia(String bucketName) throws IOException {
		Storage storage;
		Resource resource = new ClassPathResource("config/" + env.getProperty("google-cloud.storage.credential-file"));

		storage = StorageOptions.newBuilder().setProjectId(env.getProperty("google-cloud.storage.project-id"))
				.setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream())).build().getService();
		
		Bucket bucket = storage.get(bucketName);
		
		if (bucket == null || !bucket.exists()) {
			return;
		}
		
		for (Blob blob : bucket.list().iterateAll()) {
			// Compare to delete
			System.out.println(blob.getName());
		}
	}
}
