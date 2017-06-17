using DataAccessLayer.DbContext;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServiceLogicLayer.DTO
{
    public class BranchProfessorDisciplineDTO
    {
        public List<ProfessorDiscipline> sciProfessorDisciplines { get; set; }
        public List<ProfessorDiscipline> sportProfessorDisciplines { get; set; }
        public List<ProfessorDiscipline> artProfessorDisciplines { get; set; }
        public List<ProfessorDiscipline> societyProfessorDisciplines { get; set; }
        public List<StudentProfessorDiscipline> studentProfessorDisciplines { get; set; }
    }
}
