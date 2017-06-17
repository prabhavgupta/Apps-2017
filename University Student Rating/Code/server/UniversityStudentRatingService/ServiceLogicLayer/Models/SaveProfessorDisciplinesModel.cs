using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.Models
{
    public class SaveProfessorDisciplinesModel
    {
        public int ProfessorId { get; set; }
        public List<ProfessorDiscipline> ProfessorDisciplines { get; set; }
    }
}
