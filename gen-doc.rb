# needs sudo gem install nokogiri bluecloth
%w[rubygems bluecloth nokogiri].each { |x| require x }
text = Nokogiri::HTML(BlueCloth.new(File.open('cli-distro/doc/CLI.md', 'rb').read).to_html).text
File.open('cli/src/main/resources/com/gooddata/processor/COMMANDS.txt','w') { |f| f.write(text) }
