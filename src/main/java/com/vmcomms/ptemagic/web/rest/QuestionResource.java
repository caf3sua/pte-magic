package com.vmcomms.ptemagic.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;
import com.vmcomms.ptemagic.web.rest.util.HeaderUtil;
import com.vmcomms.ptemagic.web.rest.util.PaginationUtil;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Question.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    private final QuestionService questionService;

    public QuestionResource(QuestionService questionService) {
        this.questionService = questionService;
    }

    /**
     * POST  /questions : Create a new question.
     *
     * @param questionDTO the questionDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new questionDTO, or with status 400 (Bad Request) if the question has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/questions")
    @Timed
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to save Question : {}", questionDTO);
        if (questionDTO.getId() != null) {
            throw new BadRequestAlertException("A new question cannot already have an ID", ENTITY_NAME, "idexists");
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /questions : Updates an existing question.
     *
     * @param questionDTO the questionDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated questionDTO,
     * or with status 400 (Bad Request) if the questionDTO is not valid,
     * or with status 500 (Internal Server Error) if the questionDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/questions")
    @Timed
    public ResponseEntity<QuestionDTO> updateQuestion(@RequestBody QuestionDTO questionDTO) throws URISyntaxException {
        log.debug("REST request to update Question : {}", questionDTO);
        if (questionDTO.getId() == null) {
            return createQuestion(questionDTO);
        }
        QuestionDTO result = questionService.save(questionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, questionDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /questions : get all the questions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of questions in body
     */
    @GetMapping("/questions")
    @Timed
    public ResponseEntity<List<QuestionDTO>> getAllQuestions(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Questions");
        Page<QuestionDTO> page = questionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/questions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /questions/:id : get the "id" question.
     *
     * @param id the id of the questionDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the questionDTO, or with status 404 (Not Found)
     */
    @GetMapping("/questions/{id}")
    @Timed
    public ResponseEntity<QuestionDTO> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        QuestionDTO questionDTO = questionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(questionDTO));
    }

    /**
     * DELETE  /questions/:id : delete the "id" question.
     *
     * @param id the id of the questionDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/questions/{id}")
    @Timed
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
//    private void processUploadToCloud(FeedItemDTO feedItemDTO) throws IOException {
//    	Long currentTime = System.currentTimeMillis();
//    	Storage storage;
//		Resource resource = new ClassPathResource("config/blast-api.json");
//		
//		storage = StorageOptions.newBuilder()
//				.setProjectId("blast-api")
//			    .setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream()))
//			    .build()
//			    .getService();
//		
//		// Create a bucket
//		String bucketName = "itsol-blast";
//		
//		String filename = currentTime + "_" + feedItemDTO.getFilename();
//		BlobId blobId = BlobId.of(bucketName, filename);
//		
//		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(feedItemDTO.getContentType()).build();
//		
//		byte[] content = Base64.decodeBase64(feedItemDTO.getData());
//		storage.create(blobInfo, content);
//		storage.createAcl(blobId, Acl.of(User.ofAllUsers(), Role.READER));
//		
//		// Process resize to create thumb
//		String thumbData = null;
//		BufferedImage originBufImage = decodeToImage(feedItemDTO.getData());
//		
//		String imageUrl = "https://storage.googleapis.com/" + bucketName + "/" + filename;
//		String imageThumbUrl = null; //"https://storage.googleapis.com/" + bucketName + "/" + thumbFilename;
//		// Check size image
//		if (originBufImage.getWidth() > BlastConstant.IMAGE_RESIZE_WIDTH 
//				&& originBufImage.getHeight() > BlastConstant.IMAGE_RESIZE_HEIGHT) {
//			thumbData = createThumbnailImage(originBufImage
//					, BlastConstant.IMAGE_RESIZE_WIDTH, BlastConstant.IMAGE_RESIZE_HEIGHT, getFormatName(feedItemDTO.getContentType()));
//			// Create thumb
//			byte[] contentThumb = Base64.decodeBase64(thumbData);
//			
//			// Create thumb and upload
//			String thumbFilename = currentTime + "_thumb_" + feedItemDTO.getFilename();
//			BlobId blobIdThumb = BlobId.of(bucketName, thumbFilename);
//			
//			BlobInfo blobInfoThumb = BlobInfo.newBuilder(blobIdThumb).setContentType(feedItemDTO.getContentType()).build();
//			
//			storage.create(blobInfoThumb, contentThumb);
//			storage.createAcl(blobIdThumb, Acl.of(User.ofAllUsers(), Role.READER));
//			imageThumbUrl = "https://storage.googleapis.com/" + bucketName + "/" + thumbFilename;
//		} else {
//			imageThumbUrl = imageUrl;
//		}
//		
//		feedItemDTO.setImageUrl(imageUrl);
//		feedItemDTO.setImageThumbUrl(imageThumbUrl);
//    }
}
