package com.mpaike.client.zoie.service;


import static java.util.Collections.EMPTY_LIST;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.util.Version;


import proj.zoie.api.IndexReaderFactory;
import proj.zoie.api.ZoieException;
import proj.zoie.api.ZoieIndexReader;
import proj.zoie.service.api.SearchHit;
import proj.zoie.service.api.SearchRequest;
import proj.zoie.service.api.SearchResult;

public class ZoieSearchServiceImpl<R extends IndexReader> implements MpaikeZoieSearchService {

	private static final Logger log = Logger.getLogger(ZoieSearchServiceImpl.class);
	
	private IndexReaderFactory<ZoieIndexReader<R>> _idxReaderFactory;
	static int maxHits = 150;//maydaily最大图片个数
	public ZoieSearchServiceImpl(IndexReaderFactory<ZoieIndexReader<R>> idxReaderFactory){
		_idxReaderFactory=idxReaderFactory;
	}
	
	private static Map<String,String[]> convert(Document doc)
	{
		Map<String,String[]> map=new HashMap<String,String[]>();
		if (doc!=null)
		{
			List<Fieldable> fields=(List<Fieldable>)doc.getFields();
			Iterator<Fieldable> iter=fields.iterator();
			while(iter.hasNext())
			{
				Fieldable fld=iter.next();
				String fieldname=fld.name();
				map.put(fieldname, doc.getValues(fieldname));
			}
		}
		return map;
	}
	
	public SearchResult search(SearchRequest req) throws ZoieException{
		String queryString = req.getQuery();
		Analyzer analyzer = _idxReaderFactory.getAnalyzer();
		QueryParser qparser = new QueryParser(Version.LUCENE_CURRENT, "content", analyzer);
		
		SearchResult result=new SearchResult();
		
		List<ZoieIndexReader<R>> readers=null;

		MultiReader multiReader=null;
		Searcher searcher = null;
		try
		{
			Query q=null;
			if (queryString == null || queryString.length() ==0)
			{
				q = new MatchAllDocsQuery();
			}
			else
			{
				q = qparser.parse(queryString); 
			}
			readers=_idxReaderFactory.getIndexReaders();
			multiReader=new MultiReader(readers.toArray(new IndexReader[readers.size()]), false);
			searcher=new IndexSearcher(multiReader);
			
			long start=System.currentTimeMillis();
			TopDocs docs=searcher.search(q, null, 10);
			long end=System.currentTimeMillis();
			
			result.setTime(end-start);
			result.setTotalDocs(multiReader.numDocs());
			result.setTotalHits(docs.totalHits);
			

			ScoreDoc[] scoreDocs=docs.scoreDocs;
			ArrayList<SearchHit> hitList=new ArrayList<SearchHit>(scoreDocs.length);
			for (ScoreDoc scoreDoc : scoreDocs)
			{
				SearchHit hit=new SearchHit();
				hit.setScore(scoreDoc.score);
				int docid=scoreDoc.doc;
				
				Document doc=multiReader.document(docid);
				String content=doc.get("content");
				
				QueryScorer qs = new QueryScorer(q);
				
				SimpleHTMLFormatter formatter=new SimpleHTMLFormatter("<span class=\"hl\">","</span>");
				Highlighter hl=new Highlighter(formatter,qs); 
				String[] fragments=hl.getBestFragments(analyzer, "content",content, 1);
				
				Map<String,String[]> fields=convert(doc);
				fields.put("fragment",fragments);
				hit.setFields(fields);
				hitList.add(hit);
			}
			
			result.setHits(hitList.toArray(new SearchHit[hitList.size()]));
			return result;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
			throw new ZoieException(e.getMessage(),e);
		}
		finally
		{
			try{
			  if (searcher!=null)
			  {
				try {
					searcher.close();
				} catch (IOException e) {
					log.error(e.getMessage(),e);
				}
				finally{
				    if (multiReader!=null){
				      try{
				        multiReader.close();
				      }
				      catch(IOException e){
				        log.error(e.getMessage(),e);
				      }
				    }
				}
			  }
			}
			finally{
			  _idxReaderFactory.returnIndexReaders(readers);
			}
		}
	}

	@Override
	public List<Doc> searchMydaily() {
		String queryString = "";
		List<Doc> result = EMPTY_LIST;
		
		List<ZoieIndexReader<R>> readers=null;

		MultiReader multiReader=null;
		Searcher searcher = null;
		try
		{
			Query q=null;
			if (queryString == null || queryString.length() ==0)
			{
				q = new MatchAllDocsQuery();
			}
			
			readers=_idxReaderFactory.getIndexReaders();
			multiReader=new MultiReader(readers.toArray(new IndexReader[readers.size()]), false);
			searcher=new IndexSearcher(multiReader);
		
			TopDocs docs=searcher.search(q,maxHits);
			

			ScoreDoc[] scoreDocs=docs.scoreDocs;
			ArrayList<SearchHit> hitList=new ArrayList<SearchHit>(scoreDocs.length);
			Document doc = null;
			Map<String,Boolean> artMap = new HashMap<String,Boolean>();
			
			for (int i=0,n=scoreDocs.length;i<n;i++) {
					doc = multiReader.document(scoreDocs[i].doc);
					artMap.put(doc.get(QueryFactory.PICTURE_ID), true);
					result.add(createDoc(doc));
			}
			return result;
		}
		catch(Exception e)
		{
			log.error(e.getMessage(),e);
			try {
				throw new ZoieException(e.getMessage(),e);
			} catch (ZoieException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		finally
		{
			try{
			  if (searcher!=null)
			  {
				try {
					searcher.close();
				} catch (IOException e) {
					log.error(e.getMessage(),e);
				}
				finally{
				    if (multiReader!=null){
				      try{
				        multiReader.close();
				      }
				      catch(IOException e){
				        log.error(e.getMessage(),e);
				      }
				    }
				}
			  }
			}
			finally{
			  _idxReaderFactory.returnIndexReaders(readers);
			}
		}
		return result;
	}
	
	private static Doc createDoc(Document doc){
		Doc dc = new Doc();
		dc.setExifVersion(doc.get(QueryFactory.PICTURE_EXIFVERSION));
		dc.setFilename(doc.get(QueryFactory.PICTURE_FILENAME));
		dc.setFileSize(doc.get(QueryFactory.PICTURE_FILESIZE));
		dc.setFileType(doc.get(QueryFactory.PICTURE_FILETYPE));
		dc.setGeneContent(doc.get(QueryFactory.PICTURE_GENECONTENT));
		dc.setGeneDefaultValue(doc.get(QueryFactory.PICTURE_GENEDEFAULTVALUE));
		dc.setPictureId(doc.get(QueryFactory.PICTURE_ID));
		dc.setSrcHeight(doc.get(QueryFactory.PICTURE_SRCHEIGHT));
		dc.setSrcWidth(doc.get(QueryFactory.PICTURE_SRCWIDTH));
		dc.setUrl(doc.get(QueryFactory.PICTURE_URL));
		return dc;
	}
}

