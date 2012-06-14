<%@ include file="/common/taglibs.jsp"%>

<head>
<script language="javascript">window.top.skbConfigBasePath = "/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/"</script>
<script language='javascript' src='/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/net.i2geo.skillstextbox.SkillsTextBox.nocache.js'></script>
</head>



Open <a target="curriculum-browsing" 
  href='xxx' 
  onclick='window.open("/comped/showCompetency.html?uri=Justify_solution", "curriculumBrowsing","width=300,height=500,screenX=20,scrollbars=yes,status=no,toolbar=no,menubar=no,location=no,resizable=yes"); return false;'
  >CompEd Window</a>  in a new window.<p/>
then, things will be chosen into the embedded skills-text-box here...<p/>

<script type="text/javascript">
          window.skbPleaseReplaceMeActive =  "SkillsTextBox|idsStorage| competency,topic |true";
          window.skbConfigBasePath = "/SearchI2G/net.i2geo.skillstextbox.SkillsTextBox/";
          window.browserLanguages = "fr-fr";
      </script>
  <form action="" onsubmit="return false;">
      <div id="SkillsTextBox"></div>
      <!-- <p><a href="javascript:window.skbEdit('SkillsTextBox','idsStorage', '','false'); this.setVisible(false);">edit</a></p> -->
      <!-- <input id="idsStorage" name="idsStorage_" value=""/> -->
      <!-- <p>makes strings:</p> -->
      <input id="idsStorage" name="idsStorage_" value="" size="100"
      type="hidden"/>
  </form>

</div>
