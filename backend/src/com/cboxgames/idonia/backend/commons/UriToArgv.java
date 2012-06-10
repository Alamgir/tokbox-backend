package com.cboxgames.idonia.backend.commons;

public class UriToArgv {
	private String[] _argv;
	private int _base_index;
	
	public String[] getArgv() { return _argv; }
	public int getBaseIndex() { return _base_index; }
	
	public UriToArgv(String uri, String base) {
       	_argv = uri.split("/");
       	_base_index = 0;
       	while (_base_index < _argv.length) {
       		if (_argv[_base_index].equals(base) == true)
       			break;
       		_base_index++;
       	}
	}

	public static boolean verifyUrl(String uri, String part1, String part2) {	
		UriToArgv uta = new UriToArgv(uri, part1);
		int indx = uta.getBaseIndex();
		String[] argv = uta.getArgv();
		if (indx++ >= argv.length) return false;
		
		if (indx >= argv.length) {
			return (part2 != null) ? false : true;
		}
		
		return argv[indx].equals(part2) ? true : false;

	}
}
